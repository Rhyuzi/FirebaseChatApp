package com.azi.firebasechat.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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

        var intent = getIntent()
        var userId = intent.getStringExtra("userId")
        var username = intent.getStringExtra("userName")

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
}