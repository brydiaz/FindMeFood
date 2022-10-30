package com.bios.findmefood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import android.widget.TextView

class PlaceActivity : AppCompatActivity() {
    private var db = DataBaseControl()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place)
        var keyName = intent.getStringExtra("keyName")
        val txtName = findViewById<TextView>(R.id.name_place_txt)
        val ratingBar = findViewById<RatingBar>(R.id.rating_place_bar)
        val descriptionName = findViewById<TextView>(R.id.description_place_txt)
        db.getPlace(keyName.toString(), txtName,descriptionName, ratingBar)

    }
    fun back(view: View) {
        var home = Intent(this, MapsActivity::class.java)
        startActivity(home)
    }


    fun makeCalification(view:View) {
        var keyName = intent.getStringExtra("keyName")
        val ratingBar = findViewById<RatingBar>(R.id.yourRating)
        db.addCalification(keyName.toString(), ratingBar.rating.toDouble())
        var home = Intent(this, MapsActivity::class.java)
        startActivity(home)

    }
}