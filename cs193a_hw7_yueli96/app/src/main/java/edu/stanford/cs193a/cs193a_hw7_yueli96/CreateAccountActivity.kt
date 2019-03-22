package edu.stanford.cs193a.cs193a_hw7_yueli96

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_create_account.*

class CreateAccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
    }

    //sign up with firebase authentication
    fun onSignUp(view: View){
        val username = user_name_input.text
        val password = password_input.text
        if(!username.isEmpty() && !password.isEmpty()){
            Log.d("create", username.toString())
            Log.d("create", password.toString())
            WelcomeActivity.auth.createUserWithEmailAndPassword(username.toString(), password.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // account creation was successful
                        Toast.makeText(this, R.string.sign_up_success, Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, R.string.sign_up_error, Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}
