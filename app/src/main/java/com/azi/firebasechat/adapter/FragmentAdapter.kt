package com.azi.firebasechat.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.azi.firebasechat.fragment.ContactFragment
import com.azi.firebasechat.fragment.ListUserChatFragment
import com.azi.firebasechat.fragment.SettingFragment

class FragmentAdapter(fragmentAdapter: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentAdapter, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        if (position == 0){
            return ListUserChatFragment()
        }else if(position == 1){
            return ContactFragment()
        }else{
            return SettingFragment()
        }
    }
}