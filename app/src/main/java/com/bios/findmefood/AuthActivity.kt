package com.bios.findmefood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        //Setup
        setup()
    }
    /*
       In: -
       Out: -
       Restrictions: -
       Function: Make the user validation and call or not the function to show next intent
     */
    private fun setup() {
        title = "Autentication"
        val buttonAcc = findViewById<Button>(R.id.log_in_button)

        val email = findViewById<EditText>(R.id.email_edit_text)
        val password = findViewById<EditText>(R.id.email_edit_text)


        buttonAcc.setOnClickListener{
            if(email.text.isNotEmpty() && password.text.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.text.toString(),
                    password.text.toString()).addOnCompleteListener{
                    if (it.isSuccessful ) {
                        showMain()
                    } else {
                        showAlert()
                    }
                }
            }
        }
    }

    /*
       In: -
       Out: -
       Restrictions: -
       Function: Start one dialog alert
     */
    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando")
        builder.setPositiveButton("Aceptar",null)
        val dialog:AlertDialog = builder.create()
        dialog.show()
    }

    /*
       In: -
       Out: -
       Restrictions: -
       Function: Start the main activity
     */
    private fun showMain() {
        val mainIntent = Intent(this, MainActivity::class.java).apply {}
        startActivity(mainIntent)
    }
}