package com.bios.findmefood

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class NewPlaceActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val db = DataBaseControl()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setContentView(R.layout.activity_new_place)
        setup()
    }

    /*
      In: -
      Out: -
      Restrictions: -
      Function: Setup all the settings
    */
    private fun setup() {
        val backButton = findViewById<Button>(R.id.back_to_maps)
        val addButton = findViewById<Button>(R.id.make_calification_btt)
        backButton.setOnClickListener {
            onBackPressed()
        }
        addButton.setOnClickListener {
            addPlace()
        }

    }

    fun addPlace()  {
        var locationReturn:Pair<Double, Double>  = 0.0 to 0.0
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
                }
                fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if(location == null) {
                        Toast.makeText(applicationContext, "Null location",
                            Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, "Locacion obtenida!",
                            Toast.LENGTH_SHORT).show()
                        val name = findViewById<EditText>(R.id.name_place_txt)
                        val description = findViewById<EditText>(R.id.description_place_txt)
                        val calification = findViewById<RatingBar>(R.id.rating_place_bar)
                        db.addPlace(name.text.toString(), description.text.toString(),
                            calification.rating, location.latitude, location.longitude)
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
                addPlace()
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
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE)
                as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}