package com.dicoding.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dicoding.githubuser.data.local.entity.FavUserEntity

@Dao
interface FavUserDao {
    @Query("SELECT * FROM favUser")
    fun getAllFavUser(): LiveData<List<FavUserEntity>>

    @Query("SELECT EXISTS(SELECT * FROM favUser WHERE username = :username)")
    fun getFavUser(username: String): LiveData<Boolean>

    @Query("DELETE FROM favUser WHERE username = :username")
    suspend fun deleteUser(username: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: FavUserEntity?)
}