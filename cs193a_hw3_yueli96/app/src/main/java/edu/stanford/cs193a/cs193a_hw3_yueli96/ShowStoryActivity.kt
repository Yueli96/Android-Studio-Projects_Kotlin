package edu.stanford.cs193a.cs193a_hw3_yueli96

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import kotlinx.android.synthetic.main.activity_show_story.*
import android.text.method.ScrollingMovementMethod
import android.util.Log


class ShowStoryActivity : AppCompatActivity() {

    lateinit var tts: TextToSpeech

    // Display the story and initialize text to speech
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_story)
        val story = intent.getSerializableExtra("story")

        if(story != null){
            my_story.setText(story.toString())
            my_story.setMovementMethod(ScrollingMovementMethod())
            tts = TextToSpeech(this, TextToSpeech.OnInitListener {
                Log.d("output", "ttsReady")
                tts.speak(story.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
            })
        }
    }

    // on click button to stop the activity
    fun makeAnotherStory(view: View){
        finish()
    }
}
