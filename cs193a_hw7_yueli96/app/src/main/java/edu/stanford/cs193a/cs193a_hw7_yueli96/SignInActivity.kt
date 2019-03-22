package edu.stanford.cs193a.cs193a_hw7_yueli96

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {
    private lateinit var google: GoogleSignInClient
    private val REQUEST_SIGN_IN_GOOGLE = 1932

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        sign_in_google.setOnClickListener {
            onGoogleSignIn()
        }
        //retrieve account info from preference
        val prefs = getSharedPreferences("default_user", MODE_PRIVATE)
        user_name_input.setText(prefs.getString("username", ""))
        password_input.setText(prefs.getString("password", ""))
    }

    //sign in on click method
    fun onSignIn(view: View){
        val username = user_name_input.text
        val password = password_input.text
        if(!username.isEmpty() && !password.isEmpty()){
            if(remember_me_checkbox.isChecked){
                // store account info to preference
                val prefs = getSharedPreferences("default_user", MODE_PRIVATE)
                val editor = prefs.edit()
                editor.putString("username", username.toString())
                editor.putString("password", password.toString())
                editor.apply()
            }
            WelcomeActivity.auth.signInWithEmailAndPassword(username.toString(), password.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // account creation was successful
                        Toast.makeText(this, R.string.sign_in_success, Toast.LENGTH_SHORT).show()
                        WelcomeActivity.userEmail = username.toString()
                        val intent = Intent(this, ChannelListActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, R.string.sign_in_error, Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    //sign in with google
    fun onGoogleSignIn(){
        // request the user's ID, email address, and basic profile
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("firebase_web_client_id_for_google")
            .requestEmail()
            .build()
        // build API client with access to Sign-In API and options above
        google = GoogleSignIn.getClient(this, options)
        startActivityForResult(google.signInIntent, REQUEST_SIGN_IN_GOOGLE)
    }

    //google sign in result
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == REQUEST_SIGN_IN_GOOGLE) {
            if (resultCode == Activity.RESULT_OK) {
                // successfully signed in
                val account = GoogleSignIn.getSignedInAccountFromIntent(intent).result!!
                firebaseAuthWithGoogle(account)
            } else {
                // sign-in failed
                Log.d("resultcode", resultCode.toString())
                Toast.makeText(this, R.string.sign_in_error, Toast.LENGTH_LONG).show()
            }
        }
    }

    //sign in with firebase using google account
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("signin", "firebaseAuthWithGoogle:" + acct.id!!)
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        WelcomeActivity.auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    Toast.makeText(this, R.string.sign_in_success, Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, R.string.sign_in_error_google, Toast.LENGTH_LONG).show()
                }

            }
    }
}
