package com.azi.firebasechat.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.azi.firebasechat.R
import com.azi.firebasechat.model.Contact
import com.azi.firebasechat.model.UsersResponse
import com.azi.firebasechat.model.UsersResponseBody
import de.hdodenhof.circleimageview.CircleImageView

class ContactAdapter(private val context: Context, private val contactList: List<UsersResponseBody>) :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contactList[position]
        holder.txtUserName.text = contact.display_name

        Log.d("contact.display_name",contact.display_name)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtUserName: TextView = view.findViewById(R.id.userName)
        val txtTemp: TextView = view.findViewById(R.id.temp)
    }
}