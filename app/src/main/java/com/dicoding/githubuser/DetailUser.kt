package com.dicoding.githubuser

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubuser.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUser : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private val MainViewM by viewModels<MainViewModel>()
    private lateinit var username: String

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        username = intent.getStringExtra("username").toString()

        val sectionsPagerAdapter = PageAdapter(this)
        sectionsPagerAdapter.username = username
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.title = getString(R.string.detailUserTitle)

        MainViewM.getUser(username)

        MainViewM.user.observe(this, {
            setUser(it)
        })

        MainViewM.isLoading.observe(this,{
            showLoading(it)
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbDetail.visibility = View.VISIBLE
        } else {
            binding.pbDetail.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUser(user: ResponseDetail){
        binding.tvDetail.text = user.login
        binding.tvFullName.text = user.name
        binding.tvFollowers.text = "${user.followers} followers"
        binding.tvFollowing.text = "${user.following} following"
        Glide.with(this@DetailUser)
            .load(user.avatarUrl)
            .into(binding.imgDetail)
        Log.d("asd", "${user}")
    }
}