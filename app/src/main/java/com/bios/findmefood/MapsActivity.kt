package com.bios.findmefood

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.get
import com.bios.findmefood.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val db = DataBaseControl()
    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "Nuestro Mapa!"
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        spinner = findViewById<Spinner>(R.id.places_spinner)
        db.loadPlaces(spinner, this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    /*
       In: An instance of google map
       Out: -
       Restrictions: -
       Function: Google API func, when the map is ready, do something
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker in Sydney and move the camera
        setup()
        getCurrentLocation(mMap)
        mMap.setOnMarkerClickListener { marker -> // on marker click we are getting the title of our marker
            // which is clicked and displaying it in a toast message.
            val seePlace = Intent(this, PlaceActivity::class.java)
            val markerName = marker.title
            seePlace.putExtra("keyName",markerName)
            startActivity(seePlace)
            false
        }


    }
    /*
       In: -
       Out: -
       Restrictions: -
       Function: Setup all the settings
     */
    private fun setup() {
        val backButton = findViewById<Button>(R.id.back_to_main_button)
        backButton.setOnClickListener {
            val main = Intent(this, MainActivity::class.java)
            startActivity(main)
        }
        val searchButton = findViewById<Button>(R.id.search_place_button)
        searchButton.setOnClickListener {
            searchPlace()
        }

    }

    /*
       In: An instance of google map
       Out: -
       Restrictions: -
       Function: Check if we have all the permissions, if exists draw our position
     */
    private fun getCurrentLocation(gmap: GoogleMap) {
        if (checkPermissions()) {
            if(isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions()
                    return
                }
                fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location:Location? = task.result
                    if(location == null) {
                        Toast.makeText(applicationContext, "Null location",
                            Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, "Locacion obtenida!",
                            Toast.LENGTH_SHORT).show()
                        //Here we put our market
                        createMark(gmap, location.latitude,location.longitude)

                        db.getAllPlaces(gmap)





                    }
                }

            } else {
                Toast.makeText(applicationContext, "Necesitas encender tu locacion",
                    Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }


    }
    /*
       In: -
       Out: -
       Restrictions: -
       Function: request the permission
     */
    fun requestPermissions() {
        ActivityCompat.requestPermissions(this,
        arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION),
        PERMISSION_REQUEST_LOCATION)
    }

    companion object {
        private const val PERMISSION_REQUEST_LOCATION = 100
    }

    /*
       In: -
       Out: -
       Restrictions: -
       Function: request the permission
     */
    fun checkPermissions() : Boolean {
        return (ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
    }

    /*
       In: -
       Out: -
       Restrictions: -
       Function: request the permission
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "Granted",Toast.LENGTH_SHORT).show()
                getCurrentLocation(mMap)
            }
        } else {
            Toast.makeText(applicationContext, "Denied",Toast.LENGTH_SHORT).show()

        }
    }

    /*
       In: -
       Out: -
       Restrictions: -
       Function: Check if the location is enable
     */
     fun isLocationEnabled():Boolean {
        val locationManager:LocationManager = getSystemService(Context.LOCATION_SERVICE)
                as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
    /*
       In: -
       Out: -
       Restrictions: -
       Function: Create a mark in the map, in this case, with our position
     */
    private fun createMark(gmap:GoogleMap, lat:Double, long:Double) {
        val coor = LatLng(lat, long)
        val marker = MarkerOptions().position(coor).title("Mi posicion")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.position))
        gmap.addMarker(marker)
        gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(coor, 18f))
    }
    fun searchPlace() {
        val seePlace = Intent(this, PlaceActivity::class.java)
        val markerName = spinner.selectedItem.toString()

        seePlace.putExtra("keyName",markerName)
        startActivity(seePlace)
    }
}