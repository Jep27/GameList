package com.example.gamelist.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.gamelist.data.GameDao
import com.example.gamelist.model.Game
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameListViewModel (
    private val gameDao: GameDao
): ViewModel() {

    val allGames: LiveData<List<Game>> = gameDao.getAll().asLiveData()

    fun getGame(id:Long): LiveData<Game> {
        return gameDao.getById(id).asLiveData()
    }

    fun addGame(
        gameName: String,
        gameDetail: String,
        gameGenre: String,
        gameRating: Int
    ) {
        val game = Game(
            gameName = gameName,
            gameDetails = gameDetail,
            gameGenre = gameGenre,
            gameRating = gameRating
        )

        viewModelScope.launch(Dispatchers.IO){
            gameDao.insertGame(game)
        }
    }

    fun updateGame(
        id: Long,
        gameName: String,
        gameDetail: String,
        gameGenre: String,
        gameRating: Int
    ) {
        val game = Game(
            id = id,
            gameName = gameName,
            gameDetails = gameDetail,
            gameGenre = gameGenre,
            gameRating = gameRating
        )
        viewModelScope.launch(Dispatchers.IO) {
            gameDao.updateGame(game)
        }
    }

    fun deleteGame(game: Game) {
        viewModelScope.launch(Dispatchers.IO) {
             gameDao.deleteGame(game)
        }
    }

    fun isValidEntry(gameName: String, gameDetail: String, gameGenre: String, gameRating: String): Boolean {
        return gameName.isNotBlank() && gameDetail.isNotBlank() && !gameGenre.equals("Select Genre") && gameRating.isNotBlank() && !gameRating.equals("0")
    }
}

class GameListViewModelFactory(
    private val gameDao: GameDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameListViewModel(gameDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}