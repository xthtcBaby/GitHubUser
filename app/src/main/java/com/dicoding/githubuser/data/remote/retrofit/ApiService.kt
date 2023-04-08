package com.dicoding.githubuser.data.remote.retrofit

import com.dicoding.githubuser.ItemsItem
import com.dicoding.githubuser.ui.ResponseDetail
import com.dicoding.githubuser.UserResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("search/users")
    suspend fun getUsersFromRepo(
        @Query("q") username: String
    ): Response<UserResponse>

    @GET("users/{username}")
    suspend fun getUserFromRepo(
        @Path("username") username: String
    ): Response<ResponseDetail>

    @GET("users/{username}/{method}")
    suspend fun getFollowFromRepo(
        @Path("username") username: String,
        @Path("method") method: String
    ): Response<List<ItemsItem>>
}