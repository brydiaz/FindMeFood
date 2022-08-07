package com.bios.findmefood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        val analytics : FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Integracion Completa")
        analytics.logEvent("InitScreen", bundle)

        //Setup
        setup()
    }
    private fun setup() {
        title = "Autentication"
        val button = findViewById<Button>(R.id.signUnBtt)
        val buttonAcc = findViewById<Button>(R.id.logInBtt)

        val email = findViewById<EditText>(R.id.emailText)
        val password = findViewById<EditText>(R.id.emailText)

        button.setOnClickListener{
            if(email.text.isNotEmpty() && password.text.isNotEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.text.toString(),
                    password.text.toString()).addOnCompleteListener{
                        if (it.isSuccessful ) {
                            showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                        } else {
                            showAlert()
                        }
                }
            }
        }

        buttonAcc.setOnClickListener{
            if(email.text.isNotEmpty() && password.text.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.text.toString(),
                    password.text.toString()).addOnCompleteListener{
                    if (it.isSuccessful ) {
                        showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                    } else {
                        showAlert()
                    }
                }
            }
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando")
        builder.setPositiveButton("Aceptar",null)
        val dialog:AlertDialog = builder.create()
        dialog.show()
    }
    private fun showHome(email: String, provider:ProviderType) {
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email);
            putExtra("provider", provider);

        }
        startActivity(homeIntent)
    }
}