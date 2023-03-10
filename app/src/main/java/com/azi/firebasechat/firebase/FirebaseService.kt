package com.azi.firebasechat.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.azi.firebasechat.R
import com.azi.firebasechat.activity.ChatActivity
import com.azi.firebasechat.activity.UsersActivity
import com.azi.firebasechat.adapter.UserAdapter
import com.azi.firebasechat.model.Chat
import com.azi.firebasechat.model.NotificationData
import com.azi.firebasechat.model.User
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class FirebaseService: FirebaseMessagingService() {

    val CHANNEL_ID = "my_notification_channel"
    var notificationBody = ArrayList<NotificationData>()
    companion object{
        var sharedPref: SharedPreferences? = null

        var token: String?
        get() {
            return  sharedPref?.getString("token","")
        }
        set(value) {
            sharedPref?.edit()?.putString("token",value)?.apply()
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        token = p0
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
        var intent = Intent(this,UsersActivity::class.java)
        super.onMessageReceived(p0)

//        databaseReference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for (dataSnapShot: DataSnapshot in snapshot.children){
//                    val user = dataSnapShot.getValue(User::class.java)
//                    Log.d("TAGGGG TITLE",p0.data["title"].toString())
//                    if (p0.data["title"] == user!!.userName){
//                        intent.putExtra("userId",user.userId)
//                        intent.putExtra("userName",user.userName)
//
//                        Log.d("TAGGGG",user.toString())
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(applicationContext,error.message, Toast.LENGTH_SHORT).show()
//            }
//
//        })


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = Random.nextInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this,0,intent, FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle(p0.data["title"])
            .setContentText(p0.data["message"])
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationId,notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val channelName = "channelFirebaseChat"
        val channel = NotificationChannel(CHANNEL_ID,channelName, IMPORTANCE_HIGH).apply {
            description = "My Firebase Chat Description"
            enableLights(true)
            lightColor = Color.WHITE
        }
        notificationManager.createNotificationChannel(channel)
    }

}