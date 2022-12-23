package com.example.gamelist.data

import androidx.room.*
import com.example.gamelist.model.Game
import kotlinx.coroutines.flow.Flow

/**
 * Provides access to read/write operations on the schedule table.
 * Used by the view models to format the query results for use in the UI.
 */
@Dao
interface GameDao {

    @Query("SELECT * FROM game ORDER BY id ASC")
    fun getAll(): Flow<List<Game>>

    @Query("SELECT * FROM game WHERE id = :Id")
    fun getById(Id: Long): Flow<Game>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGame(game: Game)

    @Delete
    fun deleteGame(game: Game)

    @Update
    fun updateGame(game: Game)

}