package de.meadowvalley.game.storage

import de.meadowvalley.game.model.Game
import de.meadowvalley.game.model.GameState
import io.mockk.clearAllMocks
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertContains

internal class GameRepositoryInMemoryTest {

    private lateinit var gameRepository: GameRepository

    @BeforeEach
    fun beforeEach() {
        gameRepository = GameRepositoryInMemory()
    }

    @AfterEach
    fun afterEach() {
        clearAllMocks()
    }

    @Test
    fun createGame() {
        assertEquals(0, gameRepository.getAllGames().size)
        gameRepository.createGame(Game())
        assertEquals(1, gameRepository.getAllGames().size)
    }

    @Test
    fun getAllGames() {
        val game1 = Game()
        val game2 = Game()
        gameRepository.createGame(game1)
        gameRepository.createGame(game2)
        val games = gameRepository.getAllGames()
        assertEquals(2, games.size)
        assertContains(games, game1)
        assertContains(games, game2)
    }

    @Test
    fun findGame() {
        val id = 1
        val game = Game(id = id)
        assertEquals(null, gameRepository.findGame(id))
        gameRepository.createGame(game)
        assertEquals(game, gameRepository.findGame(id))
    }

    @Test
    fun findGamesInProgress() {
        val game1 = Game(gameState = GameState.OVER)
        val game2 = Game(gameState = GameState.IN_PROGRESS)
        gameRepository.createGame(game1)
        gameRepository.createGame(game2)
        val games = gameRepository.getGamesInProgress()
        assertEquals(1, games.size)
        assertContains(games, game2)
    }
}