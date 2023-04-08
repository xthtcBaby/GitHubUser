package com.dicoding.githubuser.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.dicoding.githubuser.ItemsItem
import com.dicoding.githubuser.data.local.entity.FavUserEntity
import com.dicoding.githubuser.data.local.room.FavUserDao
import com.dicoding.githubuser.data.remote.retrofit.ApiService
import com.dicoding.githubuser.ui.ResponseDetail
import com.dicoding.githubuser.utils.AppExecutors

class UserRepository private constructor(
    private var apiService: ApiService,
    private var favUserDao: FavUserDao,
    private var appExecutors: AppExecutors
) {

    suspend fun getUsers(q: String): List<ItemsItem>? {
        val response = apiService.getUsersFromRepo(q)
        if (response.isSuccessful) return response.body()?.items else return null
    }

    suspend fun getUser(q: String): ResponseDetail? {
        val response = apiService.getUserFromRepo(q)
        if (response.isSuccessful) return response.body() else return null
    }

    suspend fun getFollow(username: String, method: String): List<ItemsItem>? {
        val response = apiService.getFollowFromRepo(username, method)
        if (response.isSuccessful) return response.body() else  return null
    }

    fun getFavUsers(): LiveData<List<FavUserEntity>>{
        return favUserDao.getAllFavUser()
    }

    fun isFavoriteUser(username: String): LiveData<Boolean> {
        return favUserDao.getFavUser(username)
    }

    suspend fun deleteUser(username: String) {
        try {
            favUserDao.deleteUser(username)
        } catch (e: Exception){
            Log.d("UserRepository", "deleteUser: ${e.message.toString()} ")
        }
    }

    suspend fun saveUser(username: String) {
        try {
            val user = getUser(username)
            val newList = user?.let {
                FavUserEntity(
                    it.login,
                    user.avatarUrl
                )
            }
            favUserDao.insertUser(newList)
        } catch (e: Exception) {
            Log.d("UserRepository", "saveUser: ${e.message.toString()} ")
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            newsDao: FavUserDao,
            appExecutors: AppExecutors
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, newsDao, appExecutors)
            }.also { instance = it }
    }
}