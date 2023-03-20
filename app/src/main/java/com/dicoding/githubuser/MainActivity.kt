package com.dicoding.githubuser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val MainViewM by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

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
                if (newText != null) MainViewM.getUsers(newText.toString())
                return true
            }

        })
        return true
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