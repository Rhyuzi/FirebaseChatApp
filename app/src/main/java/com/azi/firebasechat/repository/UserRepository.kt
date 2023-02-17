package com.azi.firebasechat.repository

import com.azi.firebasechat.Room.DAO.UserDao
import com.azi.firebasechat.Room.DAO.UserDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class UserRepository(
    private val userDao: UserDao,
    private val userDatabase: UserDatabase) {
//    val allUsers: LiveData<List<User>> = userDao.getAllUsers()

    fun getUsers(){
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (user: DataSnapshot in snapshot.children){
//                        userDatabase.userDao().insertUser(user)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}