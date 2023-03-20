package com.dicoding.githubuser

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _listUser = MutableLiveData<List<ItemsItem>>()
    val listUser: LiveData<List<ItemsItem>> = _listUser

    private val _listFollowers = MutableLiveData<List<ItemsItem>>()
    val listFollowers: LiveData<List<ItemsItem>> = _listFollowers

    private val _listFollowing = MutableLiveData<List<ItemsItem>>()
    val listFollowing: LiveData<List<ItemsItem>> = _listFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _user = MutableLiveData<ResponseDetail>()
    val user: LiveData<ResponseDetail> = _user

    init {
        getUsers()
    }

    fun getUsers(q: String = "xthtcBaby") {
        _isLoading.value = true
        val client = ApiConf.getApiService().getUsers(q)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUser.value = response.body()?.items
                } else {
                    Log.e(TAG, "onFailure1: ${response}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun getUser(username: String) {
        _isLoading.value = true
        val client = ApiConf.getApiService().getUser(username)
        client.enqueue(object : Callback<ResponseDetail> {
            override fun onResponse(
                call: Call<ResponseDetail>,
                response: Response<ResponseDetail>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _user.value = response.body()
                } else {
                    Log.e(TAG, "onFailureGetuser: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseDetail>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun follow(username: String?, type: Int?) {
        _isLoading.value = true
        val method = if (type == 1) "following" else "followers"
        val client = username?.let { ApiConf.getApiService().getFollow(it, method) }
        if (client != null) {
            client.enqueue(object : Callback<List<ItemsItem>> {
                override fun onResponse(
                    call: Call<List<ItemsItem>>,
                    response: Response<List<ItemsItem>>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        if (type == 1) {
                            _listFollowing.value = response.body()
                        } else {
                            _listFollowers.value = response.body()
                        }
                    } else {
                        Log.e(TAG, "onFailure: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message}")
                }

            })
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}