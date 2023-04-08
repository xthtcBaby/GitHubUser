package com.dicoding.githubuser.data

import com.dicoding.githubuser.ui.ResponseDetail
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class UserRepositoryTest{

    lateinit var repository: UserRepository

    @Before
    fun setUp(){
        repository = Mockito.mock(UserRepository::class.java)
    }

    @Test
    fun testGetuser() = runTest{

        Mockito.`when`(repository.getUser("xthtcBaby")).thenReturn(ResponseDetail(
            99,
            "www.xthtc.img",
            100,
            "xthtcKun",
            "xthtcBaby"
        ))
        val user = repository.getUser("xthtcBaby")
        assertEquals(99,user?.followers)
        assertEquals("xthtcKun",user?.name)
    }

}