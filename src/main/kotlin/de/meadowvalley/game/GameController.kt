package de.meadowvalley.game

import de.meadowvalley.game.ai.SimpleAi
import de.meadowvalley.game.ai.SmartAi
import de.meadowvalley.game.inputmodel.HumanInputWrapper
import de.meadowvalley.game.routing.Endpoints.GAME_ENDPOINT_NAME
import de.meadowvalley.game.routing.Endpoints.GAME_WITH_ID_ENDPOINT
import de.meadowvalley.game.routing.Endpoints.SHOW_ALL_GAMES_ENDPOINT
import de.meadowvalley.game.routing.Endpoints.SHOW_ALL_GAMES_IN_PROGRESS_ENDPOINT
import de.meadowvalley.game.routing.Endpoints.START_GAME_ENDPOINT
import de.meadowvalley.game.routing.Endpoints.START_SMARTAI_GAME_ENDPOINT
import de.meadowvalley.game.routing.Responses
import de.meadowvalley.game.storage.GameRepository
import de.meadowvalley.game.storage.GameRepositoryInMemory
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route

fun Route.gameRoutes() {
    val gameRepository: GameRepository = GameRepositoryInMemory()
    val gameService = GameService(gameRepository, listOf(SimpleAi, SmartAi))

    route(GAME_ENDPOINT_NAME) {

        /**
         * This endpoint starts a new game.
         */
        get(START_GAME_ENDPOINT) {
            call.respond(gameService.createGame(smartAi = false))
        }

        get(START_SMARTAI_GAME_ENDPOINT) {
            call.respond(gameService.createGame(smartAi = true))
        }

        get(SHOW_ALL_GAMES_ENDPOINT) {
            val allGames = gameRepository.getAllGames()
            if (allGames.isEmpty()) return@get Responses.noGamesToShow(call)
            call.respond(allGames)
        }

        get(SHOW_ALL_GAMES_IN_PROGRESS_ENDPOINT) {
            val gamesInProgress = gameRepository.getGamesInProgress()
            if (gamesInProgress.isEmpty()) return@get Responses.noGamesToShow(call)
            call.respond(gamesInProgress)
        }

        get(GAME_WITH_ID_ENDPOINT) {
            val id = call.parameters["id"] ?: return@get Responses.badRequestNoId(call)
            if (!id.matches(Regex("""^\d+${'$'}"""))) return@get Responses.badRequestInvalidId(call)
            val game = gameRepository.findGame(id.toInt()) ?: return@get Responses.gameNotFoundById(call)

            call.respond(game)
        }

        post(GAME_WITH_ID_ENDPOINT) {
            val id = call.parameters["id"] ?: return@post Responses.badRequestNoId(call)
            if (!id.matches(Regex("""^\d+${'$'}"""))) return@post Responses.badRequestInvalidId(call)
            val game = gameRepository.findGame(id.toInt()) ?: return@post Responses.gameNotFoundById(call)

            try {
                val validTakeItemAmount =
                    call.receive<HumanInputWrapper>().getValidTakeItemAmount(game.gameRules.maxItemsToTake)

                if (game.isInProgress()) gameService.makePlayerMove(game, validTakeItemAmount)
                if (game.isInProgress()) gameService.makeAiMove(game)
                call.respond(game)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}

