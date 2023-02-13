package com.azi.firebasechat.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azi.firebasechat.R
import android.widget.LinearLayout
import android.widget.Toast
import com.azi.firebasechat.adapter.UserAdapter
import com.azi.firebasechat.firebase.FirebaseService
import com.azi.firebasechat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging

class UsersActivity : AppCompatActivity() {
    var userList = ArrayList<User>()
    private lateinit var userRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.users_activity)

        FirebaseService.sharedPref = getSharedPreferences("sharePref",Context.MODE_PRIVATE)
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            FirebaseService.token = it.token
        }
        userRecyclerView = findViewById<RecyclerView>(R.id.userRecyclerView)
        var imgBack = findViewById<ImageView>(R.id.imgBack)

        userRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

//        imgBack.setOnClickListener{
//            onBackPressed()
//        }
        getUserlist()
    }

    fun getUserlist(){
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

        var userid = firebase.uid
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/$userid")

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()

                for (dataSnapShot: DataSnapshot in snapshot.children){
                    val user = dataSnapShot.getValue(User::class.java)

                    if (!user!!.userId.equals(firebase.uid)){
                        userList.add(user)
                    }
                }

                var userAdapter = UserAdapter(this@UsersActivity,userList)
                userRecyclerView.adapter = userAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,error.message,Toast.LENGTH_SHORT).show()
            }

        })

    }
}