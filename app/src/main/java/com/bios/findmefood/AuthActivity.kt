package com.bios.findmefood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider

class AuthActivity : AppCompatActivity() {

    private val GOOGLE_SIGN_IN = 100

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
        val buttonGoogle = findViewById<Button>(R.id.googleLogIn_btt)

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
        buttonGoogle.setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build()
            val googleClient = GoogleSignIn.getClient(this,googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)



        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {

                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener{
                        if(it.isSuccessful ){
                            showMain()
                        } else {
                            println("holi")
                            showAlert()
                        }
                    }
                }
            } catch(e: ApiException) {
                showAlert()
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