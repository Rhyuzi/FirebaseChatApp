package com.azi.firebasechat.network

import com.azi.firebasechat.Constants.Constants
import com.azi.firebasechat.model.Chat
import com.azi.firebasechat.model.Contact
import com.azi.firebasechat.model.UsersResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface RetrofitService {

    @Headers("Authorization: syt_YXppdG9tMl9nbWFpbC5jb20_fBOxupcXzTwWDfDxNXKy_019uUD")
    @GET("user/getcontact")
    fun getData(): Call<List<UsersResponse>>
}