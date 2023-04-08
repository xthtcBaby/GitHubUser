package com.dicoding.githubuser.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.ItemsItem
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val MainViewM by viewModels<MainViewModel>(){
        ViewModelFactory.getInstance(application)
    }
    private var searchQ: String? = null
    private val mHandler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        MainViewM.getUser("smth")
        MainViewM.listUser.observe(this, { userList ->
            setUsers(userList)
        })

        MainViewM.isLoading.observe(this, {
            showLoading(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != "") {
                    searchQ = newText
                    showLoading(true)
                    mHandler.removeCallbacksAndMessages(null)
                    mHandler.postDelayed({
                        if (searchQ != "") MainViewM.getUsers(newText.toString())
                    },1000)
                }
                else{
                    searchQ = ""
                    showLoading(false)
                }
                return true
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favUser -> {
                val intentFavoriteUser = Intent(this@MainActivity, FavUserActivity::class.java)
                startActivity(intentFavoriteUser)
            }
            R.id.settings ->{
                val intentSettings = Intent(this@MainActivity,ThemeActivity::class.java)
                startActivity(intentSettings)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbMain.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setUsers(listUser: List<ItemsItem>) {
        val lUsers = ArrayList<ItemsItem>()
        for (users in listUser) {
            lUsers.addAll(listOf(users))
        }
        val adapter = UserAdapter(lUsers)
        binding.rvUser.adapter = adapter
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                showUser(data)
            }

        })
    }

    private fun showUser(data: ItemsItem) {
        val iDetailUser = Intent(this@MainActivity, DetailUser::class.java)
        iDetailUser.putExtra("username", data.login)
        startActivity(iDetailUser)
    }

}