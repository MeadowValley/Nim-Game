package de.meadowvalley.game.routing

object Endpoints {
    const val GAME_ENDPOINT_NAME = "/game"
    const val START_GAME_ENDPOINT = "/new"
    const val START_SMARTAI_GAME_ENDPOINT = "/new/smartai"
    const val GAME_WITH_ID_ENDPOINT = "/{id}"
    const val SHOW_ALL_GAMES_ENDPOINT = "/all"
    const val SHOW_ALL_GAMES_IN_PROGRESS_ENDPOINT = "$SHOW_ALL_GAMES_ENDPOINT/inprogress"
}