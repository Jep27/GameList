package com.example.gamelist.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.gamelist.getOrAwaitValue
import com.example.gamelist.model.Game
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import org.junit.Rule


@RunWith(AndroidJUnit4::class)
class GameDatabaseTest {
    private lateinit var gameDao: GameDao
    private lateinit var db: GameDatabase

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, GameDatabase::class.java
        ).allowMainThreadQueries().build()
        gameDao = db.gameDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertGame(): Unit = runBlocking {
        val game = Game(
            1,
            "GameSample",
            "GameSampleDetails",
            "GameSampleGenre",
            10
        )
        gameDao.insertGame(game)

        val games = gameDao.getAll().asLiveData().getOrAwaitValue()

        assertThat(games.contains(game))
    }

    @Test
    fun deleteGame() = runBlocking {
        val game = Game(
            1,
            "GameSample",
            "GameSampleDetails",
            "GameSampleGenre",
            10
        )
        gameDao.insertGame(game)
        gameDao.deleteGame(game)

        val games = gameDao.getAll().asLiveData().getOrAwaitValue()

        assertThat(games).doesNotContain(game)
    }
    @Test
    fun updateGame() = runBlocking {
        val game = Game(
            1,
            "GameSample",
            "GameSampleDetails",
            "GameSampleGenre",
            10
        )

        val gameUpdate = Game(
            1,
            "GameSampleUpdated",
            "GameSampleDetailsUpdated",
            "GameSampleGenreUpdated",
            11
        )
        gameDao.insertGame(game)
        gameDao.updateGame(gameUpdate)

        val games = gameDao.getAll().asLiveData().getOrAwaitValue()

        assertThat(games).doesNotContain(game)
    }
}

