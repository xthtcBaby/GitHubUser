package com.dicoding.githubuser.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.ItemsItem
import com.dicoding.githubuser.R
import com.dicoding.githubuser.data.local.entity.FavUserEntity
import com.dicoding.githubuser.databinding.ActivityFavUserBinding

class FavUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavUserBinding
    private val MainViewM by viewModels<MainViewModel>() {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.favorite_user_title)

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvFavUser.addItemDecoration(itemDecoration)

        MainViewM.getFavUsers().observe(this, { userList ->
            setUsers(userList)
            Log.d("smth", "onCreate: sini ")
        })

        MainViewM.isLoading.observe(this, {
            showLoading(it)
        })

    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbFav.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setUsers(listUser: List<FavUserEntity>) {
        val lUsers = ArrayList<ItemsItem>()
        for (users in listUser) {
            lUsers.add(
                ItemsItem(
                    users.pic,
                    users.username,
                    ""
                )
            )
        }
        val adapter = UserAdapter(lUsers)
        binding.rvFavUser.adapter = adapter
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                showUser(data)
            }

        })
    }

    private fun showUser(data: ItemsItem) {
        val iDetailUser = Intent(this@FavUserActivity, DetailUser::class.java)
        iDetailUser.putExtra("username", data.login)
        startActivity(iDetailUser)
    }


}