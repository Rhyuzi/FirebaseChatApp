package com.azi.firebasechat.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.azi.firebasechat.R
import com.azi.firebasechat.adapter.ContactAdapter
import com.azi.firebasechat.model.Contact
import com.azi.firebasechat.model.UsersResponseBody
import com.azi.firebasechat.viewModel.ContactViewModel
import com.google.gson.Gson
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class ContactFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    var contactList = ArrayList<UsersResponseBody>()
    private lateinit var contactViewModel: ContactViewModel
    private lateinit var contactRecyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

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
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)

        updateContact()

        swipeRefreshLayout.setOnRefreshListener {
            fetchData()
        }

        contactRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0){
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount

                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                    val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (visibleItemCount + firstVisibleItem >= totalItemCount) {
                        // Fetch more data here
                        updateContact()
                    }

                }
            }
        })


        return view
    }

    private fun fetchData(){
        contactRecyclerView.adapter?.notifyDataSetChanged()
        swipeRefreshLayout.isRefreshing = false

    }
    private fun onScrolledToTop() {
        updateContact()

    }

    fun updateContact(){
        contactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)

        contactViewModel.getApiHttp()
        contactViewModel.contactDataList.observe(viewLifecycleOwner,{
           initAdapter(it)
        })
    }

    private fun initAdapter(data: List<UsersResponseBody>){
        var contactAdapter = ContactAdapter(requireContext(),data)
        contactRecyclerView.adapter = contactAdapter
//        contactRecyclerView.adapter?.notifyItemRangeInserted(start, count)

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