package de.meadowvalley.game

import de.meadowvalley.game.ai.Ai
import de.meadowvalley.game.model.Game
import de.meadowvalley.game.model.GameRules
import de.meadowvalley.game.model.GameState
import de.meadowvalley.game.model.Player
import de.meadowvalley.game.model.TurnHistoryEntry
import de.meadowvalley.game.storage.GameRepositoryInMemory
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class GameServiceTest {

    @MockK
    private lateinit var gameRepositoryMock: GameRepositoryInMemory

    @MockK
    private lateinit var aiMock1: Ai

    @MockK
    private lateinit var aiMock2: Ai

    private lateinit var gameService: GameService

    @BeforeEach
    fun beforeEach() {
        gameService = GameService(gameRepositoryMock, listOf(aiMock1, aiMock2))

        every { aiMock1.isApplicable(any()) } returns true
        every { aiMock2.isApplicable(any()) } returns false
        every { aiMock1.determinePickAmount(any()) } returns 1
        every { aiMock2.determinePickAmount(any()) } returns 1
    }

    @AfterEach
    fun afterEach() {
        clearAllMocks()
    }

    @Test
    fun `WHEN game is created and ai is in turn THEN ai takes its turn and after that the player is in turn`() {
        every { gameRepositoryMock.createGame(any()) } returns Game(inTurn = Player.AI)
        val game = gameService.createGame(false)

        assertEquals(1, game.turnHistory.size)
        assertEquals(Player.AI, game.turnHistory.first().player)
        assertEquals(Player.HUMAN, game.inTurn)

        verify { aiMock1.isApplicable(game) }
        verify { aiMock1.determinePickAmount(game) }
    }

    @Test
    fun `WHEN game is created and player is in turn THEN ai does nothing`() {
        every { gameRepositoryMock.createGame(any()) } returns Game(inTurn = Player.HUMAN)
        val game = gameService.createGame(false)

        assertEquals(0, game.turnHistory.size)
        assertEquals(Player.HUMAN, game.inTurn)

        verify(exactly = 0) { aiMock1.isApplicable(any()) }
        verify(exactly = 0) { aiMock1.determinePickAmount(any()) }
    }

    @Test
    fun `WHEN moves are made by AI or human player THEN entries are added to the turnhistory`() {
        every { gameRepositoryMock.createGame(any()) } returns Game(inTurn = Player.HUMAN)
        val game = gameService.createGame(false)
        every { aiMock1.determinePickAmount(game) } returns 1

        gameService.makePlayerMove(game, 2)
        gameService.makeAiMove(game)

        assertEquals(2, game.turnHistory.size)
        assertEquals(TurnHistoryEntry(Player.HUMAN, 2, 11), game.turnHistory[0])
        assertEquals(TurnHistoryEntry(Player.AI, 1, 10), game.turnHistory[1])
    }

    @Test
    fun `WHEN player takes the last item THEN game is over and AI wins`() {
        every { gameRepositoryMock.createGame(any()) } returns Game(inTurn = Player.HUMAN)
        val game = gameService.createGame(false)
        game.turnHistory.add(TurnHistoryEntry(Player.AI, 2, 1))
        every { aiMock1.determinePickAmount(game) } returns 1

        gameService.makePlayerMove(game, 1)

        assertEquals(GameState.OVER, game.gameState)
        assertEquals(Player.AI, game.winner)
    }

    @Test
    fun `input validation`() {
        every { gameRepositoryMock.createGame(any()) } returns Game(
            inTurn = Player.HUMAN, gameRules = GameRules(maxItemsToTake = 1)
        )
        val game = gameService.createGame(false)
        every { aiMock1.determinePickAmount(game) } returns 5

        gameService.makePlayerMove(game, 2)
        gameService.makeAiMove(game)
        assertEquals(TurnHistoryEntry(Player.HUMAN, 1, 12), game.turnHistory[0])
        assertEquals(TurnHistoryEntry(Player.AI, 1, 11), game.turnHistory[1])
    }
}