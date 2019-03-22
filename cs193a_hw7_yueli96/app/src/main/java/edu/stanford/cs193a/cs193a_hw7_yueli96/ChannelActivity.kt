package edu.stanford.cs193a.cs193a_hw7_yueli96


import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_channel.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class ChannelActivity : AppCompatActivity() {
    lateinit private var channelRef: DatabaseReference
    private var messageList = ArrayList<String>()
    private var userList = ArrayList<String>()
    lateinit var messageAdapter: ArrayAdapter<String>
    lateinit var userAdapter: ArrayAdapter<String>
    lateinit var curUser: UserNode
    lateinit var mp: MediaPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel)

        // set listview adapter
        messageAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, messageList)
        message_list.adapter = messageAdapter
        userAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, userList)
        user_list.adapter = userAdapter
        mp = MediaPlayer.create(this, R.raw.text)
    }

    // get current channel name
    // set listeners for this channel node
    // add new user to this channel
    override fun onStart() {
        super.onStart()
        messageList.clear()
        userList.clear()
        getCurrentChannel()
        setListeners()
        onEnterChannel()
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    //get current channel name
    private fun getCurrentChannel(){
        var curChannelName = intent.getStringExtra("channel_name")
        if(intent.hasExtra("channel_name_from_notification")){
            curChannelName = intent.getStringExtra("channel_name_from_notification")
            Log.d("currentChannel", curChannelName)
        }
        channel_name.text = curChannelName
        WelcomeActivity.registeredChannel = curChannelName

        val fb = FirebaseDatabase.getInstance().reference

        // reference for the current channel
        channelRef = fb.child(WelcomeActivity.DATABASE).child(WelcomeActivity.DATABASE_CHANNEL)
            .child(curChannelName)
    }

    //send new messages to firebase node
    fun onMessageSend(view: View){
        val text = message_input.text
        if(!text.isEmpty()){
            val newMsg = channelRef.child("messages").push()
            val msgNode = MessageNode(curUser.email, text.toString(), getTimeStamp())
            newMsg.setValue(msgNode)
            message_input.setText("")
            mp.start()
        }
    }

    //get current timestamp
    fun getTimeStamp(): String{
        val date = Date()
        // val stringDate =  SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z", Locale.US).format(date)
        val ts = Timestamp(date.time)
        //Toast.makeText(this, ts.toString(), Toast.LENGTH_SHORT).show()
        return ts.toString()
    }

    fun onExitChannel(view: View){
        exitChannel()
    }

    //remove this user from channel member in the database
    private fun exitChannel(){
        channelRef.child("members").child(curUser.username).removeValue()
        WelcomeActivity.registeredChannel = ""
        finish()
    }

    //add this new user to channel member in the database
    private fun onEnterChannel(){
        // get current user
        curUser = UserNode(WelcomeActivity.userEmail)
        channelRef.child("members").child(curUser.username).setValue(curUser)
        Log.d("setUser", "current user: ${curUser.username}")
    }

    //set all listeners
    private fun setListeners(){

        // reference for channel members
        val memRef = channelRef.child("members")

        // reference for channel messages
        val msgRef = channelRef.child("messages")

        // set listeners for  channel members
        setListenerForChannelMembers(memRef)

        // set listener for channel messages
        val query_old = msgRef.orderByChild("timestamp").endAt(getTimeStamp())
        setListenerForOldMsg(query_old)
        val query_coming = msgRef.orderByChild("timestamp").startAt(getTimeStamp())
        setListenerForComingMsg(query_coming)
    }


    //set channel members listener
    private fun setListenerForChannelMembers(memRef: DatabaseReference){

        memRef.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val user = p0.getValue(UserNode::class.java)!!
                userList.add(user.username)
                userAdapter.notifyDataSetChanged()
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {
                val user = p0.getValue(UserNode::class.java)!!
                userList.remove(user.username)
                userAdapter.notifyDataSetChanged()
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onCancelled(p0: DatabaseError) {
                Log.d("error", "GET VALUE ERROR")
            }
        })
    }

    //set on add listener for new messages
    private fun setListenerForComingMsg(query: Query){
        query.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val msg = p0.getValue(MessageNode::class.java)!!
                messageList.add(msg.toString())
                messageAdapter.notifyDataSetChanged()
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onCancelled(p0: DatabaseError) {
                Log.d("error", "GET VALUE ERROR")
            }
        })
    }

    //set sigle event listener for old messages
    private fun setListenerForOldMsg(query: Query){
        query.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                for(msg in p0.children){
                    val node = msg.getValue(MessageNode::class.java)!!
                    messageList.add(node.toString())
                    messageAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.d("error", "GET VALUE ERROR")
            }
        })
    }

}
