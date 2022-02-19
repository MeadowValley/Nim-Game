package de.meadowvalley.game.storage

import de.meadowvalley.game.model.Game

interface GameRepository {
    fun createGame(game: Game): Game
    fun getAllGames(): List<Game>
    fun findGame(gameId: Int): Game?
    fun getGamesInProgress(): List<Game>
}