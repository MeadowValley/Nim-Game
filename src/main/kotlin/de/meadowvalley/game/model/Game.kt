package de.meadowvalley.game.model

import de.meadowvalley.game.model.GameState.IN_PROGRESS
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.concurrent.ThreadLocalRandom

@Serializable
data class Game(
    val id: Int = createId(),
    val gameRules: GameRules = GameRules(),
    val gameStartedAt: String = Instant.now().toString(),
    var gameState: GameState = IN_PROGRESS,
    var inTurn: Player = getRandomPlayer(),
    val turnHistory: MutableList<TurnHistoryEntry> = mutableListOf(),
    var winner: Player? = null,
) {
    fun isInProgress() = (gameState == IN_PROGRESS)
    fun itemCount(): Int = if (turnHistory.isEmpty()) {
        gameRules.itemCountOnStart
    } else {
        turnHistory.last().itemCount
    }

    companion object {
        private var idCounter = 0

        private fun createId(): Int {
            return ++idCounter
        }

        private fun getRandomPlayer(): Player {
            return Player.values()[ThreadLocalRandom.current().nextInt(0, Player.values().size)]
        }
    }
}