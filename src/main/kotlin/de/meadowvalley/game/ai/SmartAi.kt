package de.meadowvalley.game.ai

import de.meadowvalley.game.model.Game
import de.meadowvalley.game.model.GameRules

object SmartAi : Ai {
    override fun isApplicable(game: Game): Boolean {
        return game.gameRules.smartAi
    }

    override fun determinePickAmount(game: Game): Int {
        with(game) {
            val loseNumber = findLoseNumber(gameRules, itemCount())
            return if (loseNumber > -1 && itemCount() != loseNumber) {
                itemCount() - loseNumber
            } else {
                1
            }
        }
    }

    /**
     * If we can force the itemCount to (1 + i * (GameRules.maxItemsToTake + 1)), we will win
     */
    private fun findLoseNumber(gameRules: GameRules, itemCount: Int): Int {
        with(gameRules) {
            for (i in 0..(itemCountOnStart / maxItemsToTake)) {
                val tempLoseNumber = 1 + i * (maxItemsToTake + 1)
                if (itemCount - tempLoseNumber <= maxItemsToTake) {
                    return tempLoseNumber
                }
            }
        }
        return -1 // no lose number found
    }
}