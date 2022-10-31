package com.bios.findmefood

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.RatingBar
import android.widget.Spinner
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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
                   .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon__2___1_))
               gmap.addMarker(marker)

           }

       }
    }

    fun getPlace(toFind:String, nameField:TextView, descriptionField: TextView, ratingBar: RatingBar) {
        val data = db.collection("places").document(toFind).get().addOnSuccessListener {
            val name = it.get("name") as String
            val description = it.get("description") as String
            val rating = it.get("califications") as ArrayList<*>
            val lat = it.get("latitude") as Double
            val long = it.get("longitude") as Double
            nameField.text = name
            descriptionField.text = description
            var sumRating:Double = 0.0
            for(i in rating){
                sumRating += i as Double
            }
            val finalRating = sumRating/rating.size
            ratingBar.rating = finalRating.toFloat()
            }
    }

    fun addCalification(keyName:String, rating:Double) {
        val data = db.collection("places").document(keyName).get().addOnSuccessListener {
            val califications = it.get("califications") as ArrayList<Float>
            califications.add(rating.toFloat())
            db.collection("places").document(keyName).update("califications", califications)
        }
        }


    fun loadPlaces(spinner:Spinner, context:Context) {
        db.collection("places").get().addOnSuccessListener {
            val names = mutableListOf<String>()
            names.add("Selecciona lugar a buscar")
            for (i in it.documents) {
                val name = i.get("name") as String
                names.add(name)
            }

            if (spinner != null) {
                val adapter = ArrayAdapter(
                    context,
                    android.R.layout.simple_spinner_item, names
                )
                spinner.adapter = adapter
            }

        }
    }
}