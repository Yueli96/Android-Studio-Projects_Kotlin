// Yue Li <yueli96@stanford.edu>
// CS 193A, Winter 2019
// Homework Assignment 1
// Number Guess Game - This app thinks of a number from 1 to 1000
// and the user tries to guess it.
// This app uses toast to provide hint for user

package edu.stanford.cs193a.cs193a_hw1_yueli96

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_number_guess.*
import java.util.*

class NumberGuessActivity : AppCompatActivity() {
    private var target = 0
    private var count = 0
    private var isfinished:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_number_guess)
        pickRandomNumber()
    }

    // compare the input number with target
    fun onClickConfirm(view: View){
        val editText = findViewById<EditText>(R.id.input)
        val input = editText.text.toString()
        if(input.isEmpty() || isfinished){
            return
        }
        val guess = input.toInt()
        count++
        if(guess == target){
            counter.setText("You win! You've made $count guesses!")
            isfinished = true
        }
        else if(guess > target){
            Toast.makeText(this, "It is lower!", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(this, "It is higher!", Toast.LENGTH_SHORT).show()
        }
        editText.setText("")
    }

    // play again, pick another random number
    fun onClickPlayAgain(view: View){
        pickRandomNumber()
        count = 0
        counter.setText("")
        isfinished = false
        findViewById<EditText>(R.id.input).setText("")
    }

    // pick a random number in 1-1000
    fun pickRandomNumber(){
        val r = Random()
        target = r.nextInt(1000) + 1
        //println("target: $target")
    }
}
