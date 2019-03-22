package cs193a.stanford.edu.animalgame

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

/*
 * Yue Li <yueli96@stanford.edu>
 * CS 193A, Winter 2019
 * Homework Assignment 5
 * Animal Game - You think of an animal, the computer will
 * try to guess by asking yes or no questions
 *
 * tip: minSdkVersion 21
 *
 * Implemented Feature:
 * 1) Main activity: contain buttons for play and setting database
 * 2) Settings Activity: Allow to set up database as well as selecting the database size
 * 3) Play activity: Answer questions and update database by providing new questions
 * 4) Text to Speech: read current questions
 * 5) Provide toast when loading the database, provide animation of the question textview through 'fade in textview'
 *
 */

class AnimalGameMainActivity : AppCompatActivity() {
    companion object {
        var connected = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_game_main)
    }

    // check if the user has set up database before start to play
    fun playClick(view: View) {
        if(connected){
            val myIntent = Intent(this, PlayGameActivity::class.java)
            startActivity(myIntent)
        }
        else{
            // make a toast if database is not ready
            Toast.makeText(this, "PLEASE SET UP DATABASE", Toast.LENGTH_SHORT).show()
        }
    }

    // start database setting activity
    fun settingsClick(view: View) {
        val myIntent = Intent(this, SettingsActivity::class.java)
        startActivity(myIntent)
    }
}
