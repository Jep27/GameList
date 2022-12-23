package com.example.gamelist

import android.app.Application
import com.example.gamelist.data.GameDatabase

class GameListApplication : Application() {
    val database: GameDatabase by lazy { GameDatabase.getDatabase(this) }
}