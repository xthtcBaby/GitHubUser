package com.dicoding.githubuser.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.githubuser.ItemsItem
import com.dicoding.githubuser.data.UserRepository
import com.dicoding.githubuser.data.local.entity.FavUserEntity
import kotlinx.coroutines.launch

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {
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
        getFavUsers()
    }

    fun getUsers(q: String = "xthtcBaby") {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _listUser.value = userRepository.getUsers(q)
            } catch (e: Exception) {
                Log.e(TAG, "API call failed: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getUser(q: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _user.value = userRepository.getUser(q)
            } catch (e: Exception) {
                Log.e(TAG, "API call failed: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun follow(username: String?, type: Int?) {
        _isLoading.value = true
        viewModelScope.launch {
            if (username != null) {
                val method = if (type == 1) "following" else "followers"
                try {
                    if (type == 1) {
                        _listFollowing.value = userRepository.getFollow(username, method)
                    } else {
                        _listFollowers.value = userRepository.getFollow(username, method)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "API call failed: ${e.message}")
                } finally {
                    _isLoading.value = false
                }

            }
        }
    }

    fun getFavUsers() = userRepository.getFavUsers()

    fun isFavoriteUser(username: String) = userRepository.isFavoriteUser(username)

    fun setFav(username: String, isFav: Boolean): String{
        viewModelScope.launch {
            if (isFav)userRepository.deleteUser(username) else userRepository.saveUser(username)
        }
        return if (isFav) "User telah dihapus dari favorit" else "User telah ditambahkan kedalam favorit"
    }


    companion object {
        private const val TAG = "MainViewModel"
    }
}