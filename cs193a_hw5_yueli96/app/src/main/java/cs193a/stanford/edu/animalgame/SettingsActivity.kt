package cs193a.stanford.edu.animalgame

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

//import com.google.firebase.FirebaseApp
//import com.google.firebase.auth.FirebaseAuth


class SettingsActivity : AppCompatActivity() {
    companion object {
        private const val DATABASE_NAME_FULL = "animalgame/nodes"
        private const val DATABASE_NAME_SMALL = "animalgame_small/nodes"
        private const val LOGIN_EMAIL = "niki.liyue@gmail.com"
        private const val LOGIN_PASSWORD = "animalgame"
        private lateinit var mAuth: FirebaseAuth
        lateinit var dbr: DatabaseReference
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    // button on click call to load small database
    fun loadSmallFbClick(view: View) {
        loadFB(DATABASE_NAME_SMALL)
    }

    // button on click call to load full database
    fun loadFullFbClick(view: View) {
        loadFB(DATABASE_NAME_FULL)
    }

    // load database helper
    fun loadFB(dbName: String){
        // connect to Firebase
        FirebaseApp.initializeApp(this)
        mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithEmailAndPassword(LOGIN_EMAIL, LOGIN_PASSWORD)

        // load database
        val fb = FirebaseDatabase.getInstance().reference
        dbr = fb.child(dbName)
        AnimalGameMainActivity.connected = true
        this.finish()
    }
}
