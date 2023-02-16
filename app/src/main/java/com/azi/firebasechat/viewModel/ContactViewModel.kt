package com.azi.firebasechat.viewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azi.firebasechat.Constants.EngineUrl
import com.azi.firebasechat.model.Contact
import com.azi.firebasechat.model.UsersResponse
import com.azi.firebasechat.model.UsersResponseBody
import com.azi.firebasechat.network.RetrofitInstance
import com.azi.firebasechat.network.RetrofitService
import com.google.gson.Gson
import kotlinx.coroutines.MainScope
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ContactViewModel: ViewModel() {
    var contactDataList = MutableLiveData<List<UsersResponseBody>>()

    fun getApiData(){
        val retrofitService = RetrofitInstance.getRetrofitInstance().create(RetrofitService::class.java)
        retrofitService.getData().enqueue(object : Callback<List<UsersResponse>>{
            override fun onResponse(call: Call<List<UsersResponse>>, response: Response<List<UsersResponse>>) {
                Log.d("response.body()", response.body().toString())
//                contactDataList.value = response.body()
            }

            override fun onFailure(call: Call<List<UsersResponse>>, t: Throwable) {
                t.message?.let { Log.d("getApiData", it) }
            }

        })
    }

    fun getApiHttp(){
        val client = OkHttpClient()
        val urlApi = EngineUrl
        val request = Request.Builder()
            .url("${urlApi.BASE_URL}/corpet/user/getcontact")
            .addHeader("Authorization","syt_YXppdG9tMl9nbWFpbC5jb20_XjcKjMzHlZGGglyVcwfK_0nvzwm")
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.d("getApiContact failure", e.toString())
            }

            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val scope = MainScope()
                val gson = Gson()
                val responseBodygson = gson.fromJson(response.body()?.string(), Contact::class.java)
                    contactDataList.postValue(responseBodygson.data.body)
                    Log.d("getApiContact", responseBodygson.data.body.toString())
            }

        })
    }
}