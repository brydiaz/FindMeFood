package com.bios.findmefood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class NewPlaceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        val backButton = findViewById<Button>(R.id.back_to_main_from_add)
        backButton.setOnClickListener {
            onBackPressed()
        }
    }
}