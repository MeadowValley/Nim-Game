package de.meadowvalley.game

import de.meadowvalley.game.ai.Ai
import de.meadowvalley.game.model.Game
import de.meadowvalley.game.model.GameRules
import de.meadowvalley.game.model.GameState
import de.meadowvalley.game.model.Player.AI
import de.meadowvalley.game.model.Player.HUMAN
import de.meadowvalley.game.model.TurnHistoryEntry
import de.meadowvalley.game.storage.GameRepository

/**
 * The GameService for managing games
 */
class GameService(
    private val gameRepository: GameRepository,
    private val aiList: List<Ai>
) {

    fun createGame(smartAi: Boolean): Game {
        val game = gameRepository.createGame(Game(gameRules = GameRules(smartAi = smartAi)))
        if (game.inTurn == AI) makeAiMove(game)
        return game
    }

    fun makeAiMove(game: Game) {
        with(game) {
            val aiInput = aiList.first { it.isApplicable(game) }.determinePickAmount(game)
            makeMove(this, aiInput)
            inTurn = HUMAN
        }
    }

    fun makePlayerMove(game: Game, playerInput: Int) {
        with(game) {
            makeMove(this, playerInput)
            inTurn = AI
        }
    }

    private fun makeMove(game: Game, playerInput: Int) {
        with(game) {
            val validatedInput = getValidatedInput(itemCount(), playerInput, gameRules)
            turnHistory.add(TurnHistoryEntry(inTurn, validatedInput, itemCount() - validatedInput))

            if (itemCount() == 0) {
                gameState = GameState.OVER
                winner = if (inTurn == HUMAN) AI else HUMAN
            }
        }
    }

    private fun getValidatedInput(itemCount: Int, playerInput: Int, gameRules: GameRules): Int {
        val temp = if (playerInput > gameRules.maxItemsToTake) gameRules.maxItemsToTake else playerInput
        return if (temp > itemCount) itemCount else temp
    }
}