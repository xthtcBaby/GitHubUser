package com.dicoding.githubuser.di

import android.content.Context
import com.dicoding.githubuser.data.UserRepository
import com.dicoding.githubuser.data.local.room.FavUserDatabase
import com.dicoding.githubuser.data.remote.retrofit.ApiConf
import com.dicoding.githubuser.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConf.getApiService()
        val database = FavUserDatabase.getInstance(context)
        val dao = database.favUserDao()
        val appExecutors = AppExecutors()
        return UserRepository.getInstance(apiService, dao, appExecutors)
    }
}