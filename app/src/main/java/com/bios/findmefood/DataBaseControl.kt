package com.bios.findmefood

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

class DataBaseControl {
    private val db = FirebaseFirestore.getInstance()

    fun addPlace(name:String, description:String, calification:Float, latitude:Double, longitude:Double) {
        val califications = mutableListOf<Float>()
        califications.add(calification)
        db.collection("places").document(name).set(
            hashMapOf("name" to name,
                "description" to description,
                "califications" to califications,
                "latitude" to latitude,
                "longitude" to longitude)
        )
    }

    fun getAllPlaces(gmap:GoogleMap) {

       db.collection("places").get().addOnSuccessListener {
           val data = mutableListOf<Place>()
           for (i in it.documents) {
               val name = i.get("name") as String
               val description = i.get("description") as String
               val calification = i.get("califications") as ArrayList<*>
               val latitude = i.get("latitude") as Double
               val longitude = i.get("longitude") as Double
               data.add(Place(name,description,calification,latitude,longitude))
               val coor = LatLng(latitude, longitude)
               val marker = MarkerOptions().position(coor).title(name)
               gmap.addMarker(marker)

           }

       }
    }
}