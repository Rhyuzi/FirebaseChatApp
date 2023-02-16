package com.azi.firebasechat.Room.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.azi.firebasechat.Room.ContactEntity

@Dao
interface contactDao {
    @Query("SELECT * FROM contacts")
    fun getAll(): List<ContactEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(entity: List<ContactEntity>)
}