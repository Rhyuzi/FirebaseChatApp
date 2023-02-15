package com.azi.firebasechat.api

import android.util.Log
import com.azi.firebasechat.adapter.ContactAdapter
import com.azi.firebasechat.model.Contact
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.*
import java.io.IOException

class getApi {
    var contactList = ArrayList<Contact>()
    fun getApiContact(url: String){
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization","syt_YXppdG9tMl9nbWFpbC5jb20_fBOxupcXzTwWDfDxNXKy_019uUD")
            .build()

        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.d("getApiContact failure", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val gson = Gson()
                contactList.clear()
                val data = gson.fromJson(response.body()!!.string(), Contact::class.java)
                Log.d("getApiContact", data.data.body.toString())
            }

        })
    }
}