package edu.stanford.cs193a.cs193a_hw7_yueli96

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
/*
 * Yue Li <yueli96@stanford.edu>
 * CS 193A, Winter 2019
 * Homework Assignment 7
 * TreeChat 1.0 - This app allows you to create/enter a chatting channel
 * and send messages to all members in this channel. Use firebase as the database
 *
 * Welcome screen: Allows you to choose to sign in or sign up the with treechat
 * Sign up: Create a treechat account with valid email address and password
 * Sign in: login to treechat with your treechat account
 * *** for testing, you can use this default account:*****
 * username: niki.liyue@gmail.com
 * password: 123456
 * Channel list: view the list of current channels, or create your own channel
 * Enter Channel: current users show on the left. Messages show on the right,
 * in the order of the timestamp being sent. Click exit button on top right
 * and exit the channel(No longer receive notifications).
 *
 * Extra features:
 * 1.Localization: support Chinese
 * 2.Remember me checkbox: remember the account for future login
 * 3.Layout for landscape mode
 * 4.Sound Effect when sending a message
 *
 */
class WelcomeActivity : AppCompatActivity() {

    companion object{
        lateinit var auth: FirebaseAuth
        val DATABASE = "treechat"
        val DATABASE_CHANNEL = "channels"
        var userEmail = ""
        // last entered channel
        var registeredChannel = ""
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        // listen for broadcasts of last entered channel
        val filter = IntentFilter()
        filter.addAction("notification_complete")
        registerReceiver(NotificationReceiver(), filter)
    }

    //sign up on click method
    fun onCreateAccount(view: View){
        val intent = Intent(this, CreateAccountActivity::class.java)
        startActivity(intent)
    }

    //Sign in on click method
    fun onSignIn(view: View){
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }

    //Broadcast Receiver
    private inner class NotificationReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
        }
    }


}
