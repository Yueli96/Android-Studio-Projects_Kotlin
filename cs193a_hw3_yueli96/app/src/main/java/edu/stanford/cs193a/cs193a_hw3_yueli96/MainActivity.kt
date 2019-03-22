package edu.stanford.cs193a.cs193a_hw3_yueli96
/*
 * Yue Li <yueli96@stanford.edu>
 * CS 193A, Winter 2019
 * Homework Assignment 3
 * Mad Libs 1.0 - This app asks you to fill the words in a story
 * Note: Min SDK version for this app is 21
 * Implemented Feature:
 * 1) Main activity
 * 2) Fill in words activity
 * 3) Show story activity
 * 4) Text to speech when show_story_activity starts
 * 5) Separate layout is defined for landscape
 * 6) Status maintained when screen is rotated
 * 7) Show the number of placeholders left to fill
 */

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val storyList: MutableList<String> = ArrayList<String>()

    // set spinner adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        storyList.addAll(listOf("clothing", "dance", "marty", "tarzan", "university", "wannabe"))
        val spinnerAdapter = ArrayAdapter<String>(this, R.layout.spinner_item, R.id.item, storyList)
        story_spinner.adapter = spinnerAdapter
    }

    // start fill words activity
    fun getStarted(view: View){
        val showWordsIntent = Intent(this, FillInWordsActivity::class.java)
        showWordsIntent.putExtra("storyName", storyList.get(story_spinner.selectedItemPosition))
        startActivity(showWordsIntent)
    }

}
