package com.codingwithme.firebasechat.`interface`

import com.azi.firebasechat.Constants.Constants.Companion.CONTENT_TYPE
import com.azi.firebasechat.Constants.Constants.Companion.SERVER_KEY
import com.azi.firebasechat.model.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiEngine {

    @Headers("Authorization: syt_YXppdG9tMl9nbWFpbC5jb20_fBOxupcXzTwWDfDxNXKy_019uUD","Content-type:$CONTENT_TYPE")
    @GET("corpet/user/getcontact")
    suspend fun postNotification(
        @Body notification:PushNotification
    ): Response<ResponseBody>
}