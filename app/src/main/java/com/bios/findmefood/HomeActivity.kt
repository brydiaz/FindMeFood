package com.bios.findmefood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

enum class ProviderType {
    BASIC
}
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        setup(email ?:"", provider ?:"")
    }
    private fun setup(email:String, provider:String) {
        title = "Inicio"
        val emailView = findViewById<TextView>(R.id.emailTextView)
        val providerView = findViewById<TextView>(R.id.providerTextView)
        emailView.text = email
        providerView.text = provider
        val btt = findViewById<Button>(R.id.closeSessionBtt)
        btt.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
    }
}