package com.dicoding.githubuser.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUser : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailUserBinding
    private val mainViewModel by viewModels<MainViewModel>(){
        ViewModelFactory.getInstance(application)
    }
    private lateinit var username: String
    private var status: Boolean = false

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

        mainViewModel.getUser(username)

        mainViewModel.user.observe(this, {
            setUser(it)
        })

        mainViewModel.isLoading.observe(this,{
            showLoading(it)
        })

        val ivFavorite = binding.fabFavorite
        mainViewModel.isFavoriteUser(username).observe(this,{
            if (it){
                ivFavorite.setImageDrawable(ContextCompat.getDrawable(ivFavorite.context,R.drawable.ic_baseline_favorite_fill))
            }
            else{
                ivFavorite.setImageDrawable(ContextCompat.getDrawable(ivFavorite.context,R.drawable.ic_baseline_favorite_border))
            }
            status = it
        })

        ivFavorite.setOnClickListener(this)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    @SuppressLint("SetTextI18n")
    private fun setUser(user: ResponseDetail){
        binding.apply {
            tvDetail.text = user.login
            tvFullName.text = user.name
            tvFollowers.text = resources.getString(R.string.followers_template, user.followers)
            tvFollowing.text = resources.getString(R.string.following_template, user.following)
        }
        Glide.with(this@DetailUser)
            .load(user.avatarUrl)
            .into(binding.imgDetail)
    }

    private fun favUserAction() {
        val mes = mainViewModel.setFav(username, status)
        Toast.makeText(
            this,
            mes,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.fabFavorite -> favUserAction()
        }
    }


    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}