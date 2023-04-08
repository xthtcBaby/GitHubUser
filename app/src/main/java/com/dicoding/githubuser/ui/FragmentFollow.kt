package com.dicoding.githubuser.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.ItemsItem
import com.dicoding.githubuser.databinding.FragmentFollowBinding
import androidx.fragment.app.viewModels

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentFollow.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentFollow : Fragment() {

    private lateinit var binding: FragmentFollowBinding
    private var username: String? = null
    private var position: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(activity)
        binding.rvff.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(activity, layoutManager.orientation)
        binding.rvff.addItemDecoration(itemDecoration)


        val MainViewM: MainViewModel by viewModels{
            ViewModelFactory.getInstance(requireActivity())
        }

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }
        if (position == 1) {
            MainViewM.follow(username, position)
        } else {
            MainViewM.follow(username, position)
        }

        MainViewM.listFollowing.observe(viewLifecycleOwner, { userList ->
            setUsers(userList)
        })

        MainViewM.listFollowers.observe(viewLifecycleOwner, { userList ->
            setUsers(userList)
        })

        MainViewM.isLoading.observe(viewLifecycleOwner, {
            showLoading(it)
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbff.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setUsers(listUser: List<ItemsItem>) {
        val lUsers = ArrayList<ItemsItem>()
        for (users in listUser) {
            lUsers.addAll(listOf(users))
        }
        val adapter = UserAdapter(lUsers)
        binding.rvff.adapter = adapter
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                showUser(data)
            }

        })
    }

    private fun showUser(data: ItemsItem) {
        val iDetailUser = Intent(activity, DetailUser::class.java)
        iDetailUser.putExtra("username", data.login)
        startActivity(iDetailUser)
    }

    companion object {
        val ARG_USERNAME: String = "username"
        val ARG_POSITION: String = "position"
    }
}