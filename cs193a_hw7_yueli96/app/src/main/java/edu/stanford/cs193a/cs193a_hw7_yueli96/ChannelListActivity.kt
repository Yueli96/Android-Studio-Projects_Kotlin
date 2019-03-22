package edu.stanford.cs193a.cs193a_hw7_yueli96

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_channel_list.*

class ChannelListActivity : AppCompatActivity() {

    private val channelList = ArrayList<String>()
    lateinit var channels: DatabaseReference
    lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_list)

        // set listview adapter
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, channelList)
        channel_list.adapter = adapter

        getChannelList()

        channel_list.setOnItemClickListener { parent, view, position, id ->
            //Toast.makeText(this, "Position clicked: $position", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ChannelActivity::class.java)
            intent.putExtra("channel_name", channelList.get(position))
            startActivity(intent)
        }

        // after sign in, start notification service
        startService()
    }

    // start notification service
    private fun startService(){
        val intent = Intent(this, TreeChatService::class.java)
        intent.action = "notification"
        startService(intent)
    }


    //get current channel list from firebase
    private fun getChannelList(){
        val firebase = FirebaseDatabase.getInstance().reference
        channels = firebase.child(WelcomeActivity.DATABASE).child(WelcomeActivity.DATABASE_CHANNEL)
        channels.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val channelnode = p0.getValue(ChannelNode::class.java)!!
                channelList.add(channelnode.name)
                adapter.notifyDataSetChanged()
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

    //create channel on click method
    //add a channel to firebase
    fun onCreateChannel(view: View){
        val channelName = create_channel_name.text.toString()
        val channelDescription = create_channel_description.text.toString()
        if(!channelName.isEmpty() && !channelDescription.isEmpty()){
            val channelNode = ChannelNode(channelName, channelDescription)
            if(channelList.contains(channelName)){
                Toast.makeText(this, R.string.channel_exist_error, Toast.LENGTH_SHORT).show()
            }
            else{
                //add children to firebase, make a toast in the callback
                channels.child(channelName).setValue(channelNode){err, ref->
                    if(err == null){
                        Toast.makeText(this, R.string.channel_create_success, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        else{
            Toast.makeText(this, R.string.create_channel_error, Toast.LENGTH_SHORT).show()
        }
        create_channel_name.setText("")
        create_channel_description.setText("")
    }
}
