package de.meadowvalley.game

import de.meadowvalley.game.inputmodel.HumanInputWrapper
import de.meadowvalley.game.model.Game
import de.meadowvalley.game.model.Player
import de.meadowvalley.game.routing.Endpoints.GAME_ENDPOINT_NAME
import de.meadowvalley.game.routing.Endpoints.SHOW_ALL_GAMES_ENDPOINT
import de.meadowvalley.game.routing.Endpoints.SHOW_ALL_GAMES_IN_PROGRESS_ENDPOINT
import de.meadowvalley.game.routing.Endpoints.START_GAME_ENDPOINT
import de.meadowvalley.game.routing.Endpoints.START_SMARTAI_GAME_ENDPOINT
import de.meadowvalley.game.storage.GameRepositoryInMemory
import de.meadowvalley.module
import io.ktor.application.Application
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.withCharset
import io.ktor.server.testing.contentType
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.mockkConstructor
import io.mockk.runs
import io.mockk.verify
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.nio.charset.Charset
import kotlin.test.assertContains
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
internal class GameControllerTest {

    @BeforeEach
    fun beforeEach() {
        mockkConstructor(GameRepositoryInMemory::class)
        mockkConstructor(GameService::class)
    }

    @AfterEach
    fun afterEach() {
        clearAllMocks()
    }

    @Test
    fun `WHEN start game endpoint is called THEN a game is created and status is 200 OK`() {
        every { anyConstructed<GameService>().createGame(false) } returns Game(inTurn = Player.HUMAN)
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, GAME_ENDPOINT_NAME + START_GAME_ENDPOINT).apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
        verify { anyConstructed<GameService>().createGame(false) }
    }

    @Test
    fun `WHEN start smart ai game endpoint is called THEN a game is created and status is 200 OK`() {
        every { anyConstructed<GameService>().createGame(true) } returns Game(inTurn = Player.HUMAN)
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, GAME_ENDPOINT_NAME + START_SMARTAI_GAME_ENDPOINT).apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
        verify { anyConstructed<GameService>().createGame(true) }
    }

    @Test
    fun `WHEN show all games endpoint is called THEN all games are returned as JSON and status is 200 OK`() {
        val game1 = Game()
        val game2 = Game()
        val game3 = Game()
        val games = listOf(game1, game2, game3)
        every { anyConstructed<GameRepositoryInMemory>().getAllGames() } returns games

        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, GAME_ENDPOINT_NAME + SHOW_ALL_GAMES_ENDPOINT).apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charset.forName("UTF-8")), response.contentType())
                val gamesResponse = Json.decodeFromString<List<Game>>(response.content!!)
                assertEquals(3, gamesResponse.size)
                assertContains(gamesResponse, game1)
                assertContains(gamesResponse, game2)
                assertContains(gamesResponse, game3)
            }
        }
        verify { anyConstructed<GameRepositoryInMemory>().getAllGames() }
    }

    @Test
    fun `WHEN show all games in progress endpoint is called THEN all games in progress are returned as JSON and status is 200 OK`() {
        val game1 = Game()
        val game2 = Game()
        val game3 = Game()
        val games = listOf(game1, game2, game3)
        every { anyConstructed<GameRepositoryInMemory>().getGamesInProgress() } returns games

        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, GAME_ENDPOINT_NAME + SHOW_ALL_GAMES_IN_PROGRESS_ENDPOINT).apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charset.forName("UTF-8")), response.contentType())
                val gamesResponse = Json.decodeFromString<List<Game>>(response.content!!)
                assertEquals(3, gamesResponse.size)
                assertContains(gamesResponse, game1)
                assertContains(gamesResponse, game2)
                assertContains(gamesResponse, game3)
            }
        }
        verify { anyConstructed<GameRepositoryInMemory>().getGamesInProgress() }
    }

    @Test
    fun `WHEN get game with id endpoint is called THEN game with the matching id is returned`() {
        val id = 1
        val game = Game(id = id)
        every { anyConstructed<GameRepositoryInMemory>().findGame(id) } returns game

        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, "$GAME_ENDPOINT_NAME/$id").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charset.forName("UTF-8")), response.contentType())
                val gameResponse = Json.decodeFromString<Game>(response.content!!)
                assertEquals(game, gameResponse)
            }
        }
        verify { anyConstructed<GameRepositoryInMemory>().findGame(id) }
    }

    @Test
    fun `WHEN post game with id endpoint called THEN gameService triggers turns`() {
        val id = 1
        val game = Game(id = id)
        every { anyConstructed<GameRepositoryInMemory>().findGame(id) } returns game
        every { anyConstructed<GameService>().makeAiMove(any()) } just runs
        every { anyConstructed<GameService>().makePlayerMove(any(), any()) } just runs
        val requestJson = Json.encodeToString(HumanInputWrapper(2))

        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Post, "$GAME_ENDPOINT_NAME/$id") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(requestJson)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charset.forName("UTF-8")), response.contentType())
                val gameResponse = Json.decodeFromString<Game>(response.content!!)
                assertEquals(game, gameResponse)
            }
        }

        verify { anyConstructed<GameRepositoryInMemory>().findGame(id) }
        verify { anyConstructed<GameService>().makePlayerMove(game, 2) }
        verify { anyConstructed<GameService>().makeAiMove(game) }
    }
}