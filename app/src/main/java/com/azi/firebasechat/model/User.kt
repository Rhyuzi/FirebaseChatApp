package com.azi.firebasechat.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    var userId:String = "",
    var userName:String = "",
    var profileImage:String = "",
    var lastMessage:String = "")