package com.azi.firebasechat.Room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "contacts")
data class ContactEntity (
        var avatar_url: String = "",
        var display_name: String = "",
        var email: String = "",
        var id: Int,
        var id_user: String,
        var is_admin_center: Int,
        var is_geo: Int,
        var jenis_kelamin: String,
        var nama: String,
        var nama_jabatan: String,
        var nama_unit: String,
        var nama_wilayah: String,
        var nip: String,
        var nomor_telepon: String,
        var pin: String,
        var status: String,
        @PrimaryKey var user_id: String
        )