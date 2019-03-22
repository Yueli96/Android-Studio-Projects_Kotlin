package edu.stanford.cs193a.cs193a_hw6_yueli96

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_title_screen.*
/*
 * Yue Li <yueli96@stanford.edu>
 * CS 193A, Winter 2019
 * Homework Assignment 6
 * Snake Game
 * Implemented Feature:
 * 1) TitleScreenActivity: two buttons to start and exit the game.
 *    Show highest score record
 * 2) SnakeGameActivity: play the game, buttons for turning left and right.
 *    Buttons to restart and exit the game.
 *    Show current score and remain chances. A total of 3 chances for each game
 * 3) Portrait and landscape mode are supported
 * 4) fix grid size to fit different device sizes
 * 5) Sound effects when starting the game, eating food and losing the game
 * 6) Rotate the snake head for different walking directions
 * 7) Keyboard controls: you can use the left/right arrow keys on your PC keyboard to steer the snake
 * 8) Localization: Support English and Chinese
 * 9) A counter of 3 lives before game over.
 */
class TitleScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title_screen)
    }

    override fun onResume() {
        super.onResume()
        updateHighestScore()
    }

    // play btn onclick
    fun onPlay(view: View){
        val intent = Intent(this, SnakeGameActivity::class.java)
        startActivity(intent)
        updateHighestScore()
    }

    // exit btn onclick
    fun onExit(view: View){
        finish()
    }

    // update highest score
    fun updateHighestScore(){
        val prefs = getSharedPreferences("SCORE", MODE_PRIVATE)
        val highestScore = prefs.getInt("HIGHEST_SCORE", 0)
        score!!.text = highestScore.toString()
    }
}
