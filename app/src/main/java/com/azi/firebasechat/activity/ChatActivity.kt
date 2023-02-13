package com.azi.firebasechat.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azi.firebasechat.R
import com.azi.firebasechat.RetrofitInstance
import com.azi.firebasechat.adapter.ChatAdapter
import com.azi.firebasechat.adapter.UserAdapter
import com.azi.firebasechat.model.Chat
import com.azi.firebasechat.model.NotificationData
import com.azi.firebasechat.model.PushNotification
import com.azi.firebasechat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class ChatActivity : AppCompatActivity() {
    var firebaseUser: FirebaseUser? = null
    var reference: DatabaseReference? = null
    var chatList = ArrayList<Chat>()
    var topic = ""
    private lateinit var chatRecyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_activity)

        chatRecyclerView = findViewById<RecyclerView>(R.id.chatRecycleView)
        chatRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)



        var imgProfile = findViewById<CircleImageView>(R.id.imgProfile)
        var tvUserName = findViewById<TextView>(R.id.tvUsername)
        var imgBack = findViewById<ImageView>(R.id.imgBack)
        var btnSendMessage = findViewById<ImageButton>(R.id.btnSendMessage)
        var etMessage = findViewById<EditText>(R.id.etMessage)

        var intent = getIntent()
        var userId = intent.getStringExtra("userId")
        var username = intent.getStringExtra("userName")
        Log.d("TAGGGG",userId.toString())
        Log.d("TAGGGG",username.toString())
        firebaseUser = FirebaseAuth.getInstance().currentUser

        readMessages(firebaseUser!!.uid,userId!!)

        btnSendMessage.setOnClickListener{
            var message: String = etMessage.text.toString()
            if (message.isEmpty()){
                Toast.makeText(applicationContext,"Message is empty",Toast.LENGTH_SHORT).show()
                etMessage.setText("")
            }else{
                sendMessages(firebaseUser!!.uid,userId!!,message)
                etMessage.setText("")
                topic = "/topics/$userId"
                PushNotification(NotificationData(username!!,message),topic)
                    .also {
                        sendNotification(it)
                    }
            }

            readMessages(firebaseUser!!.uid,userId!!)
        }

        imgBack.setOnClickListener{
            onBackPressed()
        }

        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId!!)

        reference!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                tvUserName.text = username
                if (user!!.profileImage == ""){
                    imgProfile.setImageResource(R.drawable.profile_image)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun sendMessages(senderId: String, receiverId: String, message: String){
        var reference: DatabaseReference? = FirebaseDatabase.getInstance().getReference()

        var hashMap: HashMap<String,String> = HashMap()
        hashMap.put("senderId", senderId)
        hashMap.put("receiverId", receiverId)
        hashMap.put("message", message)

        reference!!.child("Chats").push().setValue(hashMap)
    }

    fun readMessages(senderId: String, receiverId: String){
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Chats")


        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for (dataSnapShot: DataSnapshot in snapshot.children){
                    val chat = dataSnapShot.getValue(Chat::class.java)

                    if (chat!!.senderId.equals(senderId) && chat!!.receiverId.equals(receiverId) ||
                        chat!!.senderId.equals(receiverId) && chat!!.receiverId.equals(senderId) ){
                        chatList.add(chat)
                    }
                }
                val chatAdapter = ChatAdapter(this@ChatActivity, chatList)

                chatRecyclerView.adapter = chatAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val result = RetrofitInstance.api.postNotification(notification)
            if (result.isSuccessful){
                Log.d("TAG", "Response: ${Gson().toJson(result)}")
            }else{
                Log.e("TAG", result.errorBody()!!.string())
            }
        }catch (e: Exception){
            Log.e("TAG", e.message.toString())
        }
    }
}