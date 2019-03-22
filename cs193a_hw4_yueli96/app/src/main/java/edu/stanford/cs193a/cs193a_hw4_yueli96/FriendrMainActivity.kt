package edu.stanford.cs193a.cs193a_hw4_yueli96

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.koushikdutta.ion.Ion
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.SignInButton
import kotlinx.android.synthetic.main.activity_friendr_main.*

/*
 * Yue Li <yueli96@stanford.edu>
 * CS 193A, Winter 2019
 * Homework Assignment 4
 * Friendr 1.0 - It app allows you to find and rate friends
 * Implemented Feature:
 * 1) Friendr Main activity
 * 2) View Users activity
 * 3) Profile activity
 * 4) LandScape Mode for view profile
 * 5) Animation effects for rating bar
 */

class FriendrMainActivity : AppCompatActivity() {

    // userLists store the name list of all users
    companion object {
        val BASE_URL = "http://www.martystepp.com/friendr/friends/"
        val TEXT_URL = BASE_URL + "list"
        lateinit var usersList: List<String>
    }
    lateinit var mp: MediaPlayer

    // set up media player
    // read in the user list
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friendr_main)
        mp = MediaPlayer.create(this, R.raw.friends_theme)
        mp.isLooping = true
        readUserList()
    }

    // start sign up activity
    fun signUpClick(view: View) {
        val myIntent = Intent(this, SignUpActivity::class.java)
        startActivity(myIntent)
    }

    // start swipe activity
    fun swipeClick(view: View) {
        val myIntent = Intent(this, SwipeUsersActivity::class.java)
        startActivity(myIntent)
    }

    // helper func for reading in the user list
    private fun readUserList() {
        Ion.with(this)
            .load(FriendrMainActivity.TEXT_URL)
            .asString()
            .setCallback { _, users ->
                usersList = users.toLowerCase().split("\n")
            }
    }

    // start viewing user activity
    fun viewUsersClick(view: View) {
        val myIntent = Intent(this, ViewUsersActivity::class.java)
        startActivity(myIntent)
    }

    // continue music when start/restart activity
    override fun onResume() {
        super.onResume()
        mp.start()
    }

    // pause music when pause activity
    override fun onPause() {
        super.onPause()
        mp.pause()
    }

}
