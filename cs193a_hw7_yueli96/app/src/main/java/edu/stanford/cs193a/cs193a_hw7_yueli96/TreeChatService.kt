package edu.stanford.cs193a.cs193a_hw7_yueli96

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import java.sql.Timestamp
import java.util.*

class TreeChatService : Service() {

    companion object {
        // ID code that is used to launch the notifications
        private const val NOTIFICATION_CHANNEL_ID = "CS193ATreeChatService"
        private const val NOTIFICATION_ID = 1234
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            if (intent.action == "notification") {
                // perform the actual work in a thread
                val thread = Thread {
                    databaseListener()
                }
                thread.start()
            }
        }

        return Service.START_STICKY
    }

    //set database message add listener
    //when new messages arrive, check if it is the current entered channel, then make notifications
    private fun databaseListener(){
        val fb = FirebaseDatabase.getInstance().reference

        // reference for the current channel
        val channelRef = fb.child(WelcomeActivity.DATABASE)
            .child(WelcomeActivity.DATABASE_CHANNEL)

        channelRef.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val channel = p0.getValue(ChannelNode::class.java)!!
                channelRef.child(channel.name).child("messages")
                    .orderByChild("timestamp").startAt(Timestamp(Date().time).toString())
                    .addChildEventListener(object: ChildEventListener{
                        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                            if(WelcomeActivity.registeredChannel.equals(channel.name)){
                                makeNotification(channel.name)
                            }
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

    //make and send notification
    private fun makeNotification(channelName: String){
        val manager = getSystemService(NOTIFICATION_SERVICE)
                as NotificationManager
        var builder = Notification.Builder(this)

        // new Android versions require us to create a notification "channel"
        // for the notification before we send it
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_ID,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)

            builder = Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
        }

        builder.setContentTitle("Treechat")
        builder.setContentText("You have a new message from $channelName")
        builder.setAutoCancel(true)
        builder.setSmallIcon(R.drawable.icon_chat)

        // when user clicks this notification, launch ChannelActivity
        val intent = Intent(this, ChannelActivity::class.java)
        intent.action = "notification_click"
        intent.putExtra("channel_name_from_notification", channelName)
        val pending = PendingIntent.getActivity(this, 0,
                                    intent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(pending)

        // send the notification
        val notification = builder.build()
        manager.notify(NOTIFICATION_ID, notification)

        val doneIntent = Intent()
        doneIntent.action = "notification_complete"
        sendBroadcast(doneIntent)
    }

    // disable bound service
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
