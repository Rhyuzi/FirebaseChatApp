package com.azi.firebasechat.model

import java.util.Objects

data class Contact(
    var data: UsersResponse,
    var error: Boolean
)


data class UsersResponse(
    val body: List<UsersResponseBody>
)

data class UsersResponseBody(
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
    var user_id: String
)
