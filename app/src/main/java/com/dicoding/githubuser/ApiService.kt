package com.dicoding.githubuser

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getUsers(
        @Query("q") username: String
    ): Call<UserResponse>

    @GET("users/{username}")
    fun getUser(
        @Path("username")  username: String
    ): Call<ResponseDetail>

    @GET("users/{username}/{method}")
    fun getFollow(
        @Path("username")  username: String,
        @Path("method")  method: String
    ): Call<List<ItemsItem>>
}