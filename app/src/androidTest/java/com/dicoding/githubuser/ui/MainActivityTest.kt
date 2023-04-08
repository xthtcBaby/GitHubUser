package com.dicoding.githubuser.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import com.dicoding.githubuser.R
import org.junit.Rule
import java.lang.Thread.sleep

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest{
    val pos1 = 0
    val pos2 = 7

    @get: Rule
    val activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testDetailUser(){
        onView(withId(R.id.pbMain)).check(matches(isDisplayed()))
        sleep(500)
        onView(withId(R.id.rv_user)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_user)).perform(RecyclerViewActions.actionOnItemAtPosition<UserAdapter.ViewHolder>(pos1,click()))
        sleep(500)
        onView(withId(R.id.rvff)).check(matches(isDisplayed()))
        onView(withId(R.id.rvff)).perform(RecyclerViewActions.actionOnItemAtPosition<UserAdapter.ViewHolder>(pos1,click()))
        sleep(500)
        onView(withId(R.id.rvff)).check(matches(isDisplayed()))
        onView(withId(R.id.rvff)).perform(RecyclerViewActions.actionOnItemAtPosition<UserAdapter.ViewHolder>(pos2,click()))
    }
}