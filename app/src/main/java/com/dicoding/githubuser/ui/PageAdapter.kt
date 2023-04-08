package com.dicoding.githubuser.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PageAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var username: String = ""

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = FragmentFollow()
        fragment.arguments = Bundle().apply {
            putInt(FragmentFollow.ARG_POSITION, position + 1)
            putString(FragmentFollow.ARG_USERNAME, username)
        }
        return fragment
    }
}