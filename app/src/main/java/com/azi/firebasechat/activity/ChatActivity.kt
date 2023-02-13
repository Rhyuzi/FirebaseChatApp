package com.azi.firebasechat.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.azi.firebasechat.R
import com.azi.firebasechat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView

class ChatActivity : AppCompatActivity() {
    var firebaseUser: FirebaseUser? = null
    var reference: DatabaseReference? = null
//    private lateinit var imgProfile: CircleImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_activity)


        var imgProfile = findViewById<CircleImageView>(R.id.imgProfile)
        var tvUserName = findViewById<TextView>(R.id.tvUsername)
        var imgBack = findViewById<ImageView>(R.id.imgBack)
        var btnSendMessage = findViewById<ImageButton>(R.id.btnSendMessage)
        var etMessage = findViewById<EditText>(R.id.etMessage)

        var intent = getIntent()
        var userId = intent.getStringExtra("userId")
        var username = intent.getStringExtra("userName")

        btnSendMessage.setOnClickListener{
            var message: String = etMessage.text.toString()
            if (message.isEmpty()){
                Toast.makeText(applicationContext,"Message is empty",Toast.LENGTH_SHORT).show()
            }else{
                sendMessages(firebaseUser!!.uid,userId!!,message)
                etMessage.setText("")
            }
        }

        imgBack.setOnClickListener{
            onBackPressed()
        }

        firebaseUser = FirebaseAuth.getInstance().currentUser
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
}