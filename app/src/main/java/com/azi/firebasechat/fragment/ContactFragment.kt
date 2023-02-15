package com.azi.firebasechat.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azi.firebasechat.Constants.EngineUrl
import com.azi.firebasechat.R
import com.azi.firebasechat.adapter.ContactAdapter
import com.azi.firebasechat.model.Contact
import com.azi.firebasechat.model.UsersResponseBody
import com.google.gson.Gson
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ContactFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContactFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var contactList = ArrayList<UsersResponseBody>()
    private lateinit var contactRecyclerView: RecyclerView

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
        var view = inflater.inflate(R.layout.fragment_contact, container, false)
        contactRecyclerView = view.findViewById<RecyclerView>(R.id.contactRecyclerView)
        contactRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)

        val urlApi = EngineUrl
        getApiContact("${urlApi.BASE_URL}/corpet/user/getcontact")
        Log.d("URL","${urlApi.BASE_URL}/corpet/user/getcontact")

        return view
    }

    fun getApiContact(url: String){
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization","syt_YXppdG9tMl9nbWFpbC5jb20_XjcKjMzHlZGGglyVcwfK_0nvzwm")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("getApiContact failure", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val scope = MainScope()
                contactList.clear()
                val gson = Gson()
                val responseBodygson = gson.fromJson(response.body()?.string(), Contact::class.java)
                for (data in responseBodygson.data.body){
                    contactList.add(data)
                    Log.d("getApiContact", data.toString())
                    Log.d("contactList 1", contactList.toString())
                }

                scope.launch {
                    var contactAdapter = ContactAdapter(requireContext(),contactList)
                    contactRecyclerView.adapter = contactAdapter
                }


            }

        })
        Log.d("contactList 2", contactList.toString())
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ContactFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ContactFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}