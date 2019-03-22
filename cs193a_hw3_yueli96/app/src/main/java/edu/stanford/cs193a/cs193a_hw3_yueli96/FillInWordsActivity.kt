package edu.stanford.cs193a.cs193a_hw3_yueli96

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_fill_in_words.*


class FillInWordsActivity : AppCompatActivity() {
    val rawStory = Story()
    var totalToFill = 0
    var indexToFill = 0

    // read the story template and set params
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fill_in_words)
        val storyName = intent.getStringExtra("storyName").toString()
        rawStory.read(this, storyName)
        totalToFill = rawStory.placeholderCount
        updatePlaceHolderCount()
        if(indexToFill < totalToFill){
            input_word.setHint("${rawStory.placeholders.get(indexToFill)}")
        }
        //Log.d("output", rawStory.toString())
    }

    // on click button to fill in a word and update display
    // if all place holders are filled, start a new activity
    fun inputWord(view: View){
        val inputWord = input_word.text.toString()
        if(!inputWord.isEmpty() && indexToFill < totalToFill){
            rawStory.fillPlaceholder(indexToFill, inputWord)
            indexToFill++
            updatePlaceHolderCount()
            input_word.setText("")
        }
        if(indexToFill == totalToFill){
            val showStoryIntent = Intent(this, ShowStoryActivity::class.java)
            showStoryIntent.putExtra("story", rawStory)
            startActivity(showStoryIntent)
        }
        else{
            input_word.setHint("${rawStory.placeholders.get(indexToFill)}")
        }
    }

    // helper, update the number of placeholders to be filled
    fun updatePlaceHolderCount(){
        val leftToFill = totalToFill - indexToFill
        words_left.setText("$leftToFill word(s) left");
    }

    // finish the activity when restart from show_story_activity
    override fun onRestart() {
        super.onRestart()
        finish()
    }
}
