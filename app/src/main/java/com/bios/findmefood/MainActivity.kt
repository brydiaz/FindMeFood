package com.bios.findmefood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
    }
    /*
       In: -
       Out: -
       Restrictions: -
       Function: Control the flows of the program depending the decision of the user
     */
    private fun setup() {
        val addPlaceButton = findViewById<Button>(R.id.add_place_button)
        val seePlacesButton = findViewById<Button>(R.id.see_places_button)
        val backButton = findViewById<Button>(R.id.back_button)

        addPlaceButton.setOnClickListener {
            val newPlace = Intent(this, NewPlaceActivity::class.java).apply {}
            startActivity(newPlace)
        }

        seePlacesButton.setOnClickListener {
            val mapIntent = Intent(this, MapsActivity::class.java).apply {}
            startActivity(mapIntent)
        }

        backButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }

    }
}