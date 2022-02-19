package de.meadowvalley.game.storage

import de.meadowvalley.game.model.Game

class GameRepositoryInMemory : GameRepository {
    private val games: MutableList<Game> = mutableListOf()

    override fun createGame(game: Game): Game {
        games.add(game)
        return game
    }

    override fun getAllGames(): List<Game> {
        return games.toList()
    }

    override fun findGame(gameId: Int): Game? {
        return games.find { it.id == gameId }
    }

    override fun getGamesInProgress(): List<Game> {
        return games.filter { it.isInProgress() }
    }
}


