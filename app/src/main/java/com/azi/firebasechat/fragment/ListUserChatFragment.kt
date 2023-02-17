package com.azi.firebasechat.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azi.firebasechat.R
import com.azi.firebasechat.Room.DAO.UserDatabase
import com.azi.firebasechat.adapter.UserAdapter
import com.azi.firebasechat.firebase.FirebaseService
import com.azi.firebasechat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class ListUserChatFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userDatabase: UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_user_chat,container,false)

        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
        userDatabase = UserDatabase.getDatabase(requireContext())

        var userid = firebase.uid
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/$userid")
        FirebaseService.sharedPref = requireContext().getSharedPreferences("sharePref", Context.MODE_PRIVATE)
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            FirebaseService.token = it.token
        }
        userRecyclerView = view.findViewById<RecyclerView>(R.id.userRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
        getUserlist()
        getData()

        return view
    }

    fun getData(){
        GlobalScope.launch {
            val userDao = userDatabase.userDao()
            val users = userDao.getAllUsers()
            withContext(Dispatchers.Main) {
                var userAdapter = UserAdapter(requireContext(),users)
                userRecyclerView.adapter = userAdapter
            }
        }
    }
    fun getUserlist(){
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

        var userid = firebase.uid

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                GlobalScope.launch {
                    for (userSnapshot: DataSnapshot in snapshot.children){
                        val userId = userSnapshot.key ?: ""
                        val userName = userSnapshot.child("userName").getValue(String::class.java) ?: ""
                        val profileImage = userSnapshot.child("profileImage").getValue(String::class.java) ?: ""
                        val lastMessage = userSnapshot.child("lastMessage").getValue(String::class.java) ?: ""
                        val user = User(userId, userName, profileImage, lastMessage)
                        userDatabase.userDao().insertUser(user)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListUserChatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}