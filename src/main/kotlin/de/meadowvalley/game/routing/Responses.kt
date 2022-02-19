package de.meadowvalley.game.routing

import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond

object Responses {
    suspend fun badRequestNoId(call: ApplicationCall) =
        call.respond(HttpStatusCode.BadRequest, "400 - No id provided.")

    suspend fun badRequestInvalidId(call: ApplicationCall) =
        call.respond(HttpStatusCode.BadRequest, "400 - Please enter a valid id as number.")

    suspend fun gameNotFoundById(call: ApplicationCall) =
        call.respond(HttpStatusCode.NotFound, "404 - No game with given id found.")

    suspend fun noGamesToShow(call: ApplicationCall) =
        call.respond(HttpStatusCode.NotFound, "404 - No games to show.")
}