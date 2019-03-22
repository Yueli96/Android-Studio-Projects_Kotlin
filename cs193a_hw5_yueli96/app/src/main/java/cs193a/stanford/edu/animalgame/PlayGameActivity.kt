package cs193a.stanford.edu.animalgame

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_play_game.*
import android.speech.tts.TextToSpeech


class PlayGameActivity : AppCompatActivity() {
    private var curNode: DBNode = DBNode()
    private var preNode: DBNode = DBNode()
    private var dbSize = 0
    lateinit var tts: TextToSpeech
    var ttsReady = false;

    // set onclick listener for buttons
    // count database node size for future updating
    // initialize text to speech api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_game)
        setYesButtonOnclickListener()
        setNoButtonOnclickListener()
        updateDBNodeSize()
        tts = TextToSpeech(this) {
            ttsReady = true;
        }
        Toast.makeText(this, "Loading database...",Toast.LENGTH_SHORT).show()
    }

    // retrieve data with specified id, update textview
    private fun getNodeWithID(id: String){
        SettingsActivity.dbr.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(ds: DataSnapshot) {
                if(ds.child(id).hasChildren()){
                    preNode = curNode
                    curNode = ds.child(id).getValue(DBNode::class.java)!!
                    if(curNode.isQuestion){
                        question_field.text = curNode.text
                    }
                    else{
                        val str = "Is it " + curNode.text + "?"
                        question_field.text = str
                    }
                    if(ttsReady){
                        tts.speak(question_field.text, TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                    Log.d("ly", curNode.toString())
                }
                else{
                    Log.d("ly", "ID does not exist")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // report/log the error
                Log.d("ly", databaseError.details)
            }
        })
    }

    // update database node size for adding nodes in the future
    private fun updateDBNodeSize(){
        dbSize = 0
        SettingsActivity.dbr.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(ds: DataSnapshot) {
                ds.children.forEach {
                    dbSize++
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // report/log the error
                Log.d("ly", databaseError.details)
            }
        })
    }

    // yes button onclick listener
    private fun setYesButtonOnclickListener(){
        yes_button.setOnClickListener {
            if(curNode.isQuestion){
                getNodeWithID(curNode.yes.toString())
            }
            else{
                Toast.makeText(this, "I WIN!",Toast.LENGTH_SHORT).show()
                setTreeGrowViewsVisible(false)
                play_again.visibility = View.VISIBLE
            }
        }
    }

    // no button onclick listener
    private fun setNoButtonOnclickListener(){
        no_button.setOnClickListener {
            if(curNode.isQuestion){
                getNodeWithID(curNode.no.toString())
            }
            else{
                Toast.makeText(this, "YOU WIN!",Toast.LENGTH_SHORT).show()
                setTreeGrowViewsVisible(true)
            }
        }
    }

    // submit button on click listener, update the database with new questions and answers
    fun submitQuestionClick(view: View) {
        val question = your_question.text.toString()
        val answer = your_object.text.toString()
        if(question.isEmpty() || answer.isEmpty()){
            return
        }
        val newQuestionNode = DBNode(question, "question", dbSize + 1, -1, dbSize + 2, curNode.id)
        val newAnswerNode = DBNode(answer, "answer", dbSize + 2, -1, -1, -1)

        dbSize += 2

        SettingsActivity.dbr.child(newQuestionNode.id.toString()).setValue(newQuestionNode)
        SettingsActivity.dbr.child(newAnswerNode.id.toString()).setValue(newAnswerNode)
        if(preNode.yes == curNode.id){
            SettingsActivity.dbr.child(preNode.id.toString()).child("yes").setValue(newQuestionNode.id)
        }
        else{
            SettingsActivity.dbr.child(preNode.id.toString()).child("no").setValue(newQuestionNode.id)
        }
        Toast.makeText(this, "UPDATED!",Toast.LENGTH_SHORT).show()
        setTreeGrowViewsVisible(false)
        play_again.visibility = View.VISIBLE
    }

    // play again button onclick listener, reload the first question
    fun playAgainClick(view: View) {
        getNodeWithID("1")
        setTreeGrowViewsVisible(false)
    }

    // set submit views (in)visible
    private fun setTreeGrowViewsVisible(visible: Boolean){
        if(visible){
            your_object.visibility = View.VISIBLE
            your_question.visibility = View.VISIBLE
            submit_question.visibility = View.VISIBLE
            play_again.visibility = View.VISIBLE
        }
        else{
            your_object.visibility = View.INVISIBLE
            your_question.visibility = View.INVISIBLE
            submit_question.visibility = View.INVISIBLE
            play_again.visibility = View.INVISIBLE
        }
    }

    // get the first node after the activity finishes loading
    override fun onPostResume() {
        super.onPostResume()
        getNodeWithID("1")
    }

}
