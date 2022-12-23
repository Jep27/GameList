package com.example.gamelist.ui.presentation

import android.content.Context
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.gamelist.MainActivity
import com.example.gamelist.R
import com.example.gamelist.data.GameDao
import com.example.gamelist.data.GameDatabase
import com.example.gamelist.data.GameSampleData
import com.example.gamelist.getOrAwaitValue
import com.example.gamelist.model.Game
import com.example.gamelist.ui.adapter.GameListAdapter
import com.example.gamelist.ui.adapter.GameListAdapter.*
import com.google.common.truth.Truth
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

class GameListFragmentTest{

    @get: Rule
    val activityRule : ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    val List_Item_Test = 10
    val Game_Test = GameSampleData.games[List_Item_Test]
    fun floatingbutton_visible(){
        onView(withId(R.id.floating_action_button_add_game)).check(matches(isDisplayed()))
    }

    @Test
    fun recycler_visible(){
        onView(withId(R.id.game_list_recycler_view)).check(matches(isDisplayed()))
    }

    @Test
    fun floatingbutton_active(){
        onView(withId(R.id.floating_action_button_add_game)).perform(
            click())
    }

    @Test
    fun recycler_scroll(){
        onView(withId(R.id.game_list_recycler_view)).perform(
            scrollToPosition<GameViewHolder>(10)
        )
    }

    @Test
    fun test_SelectedGame_Visible_GameDetailsFragment() {

        onView(withId(R.id.game_list_recycler_view))
            .perform(actionOnItemAtPosition<GameViewHolder>(
                List_Item_Test, click()))

        onView(withText(R.id.name_text_view))
            .check(matches(withText(Game_Test.gameName)))
    }
}
