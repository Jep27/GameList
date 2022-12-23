package com.example.gamelist.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.flow.Flow

/**
 * Represents a single table in the database. Each row is a separate instance of the Schedule class.
 * Each property corresponds to a column. Additionally, an ID is needed as a unique identifier for
 * each row in the database.
 */
@Entity
data class Game(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @NonNull @ColumnInfo(name = "game_name")
    val gameName: String,
    @NonNull @ColumnInfo(name = "game_details")
    val gameDetails: String,
    @NonNull @ColumnInfo(name = "game_genre")
    val gameGenre: String,
    @NonNull @ColumnInfo(name = "game_rating")
    val gameRating: Int
)