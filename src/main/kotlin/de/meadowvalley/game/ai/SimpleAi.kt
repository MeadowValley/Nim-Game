package de.meadowvalley.game.ai

import de.meadowvalley.game.model.Game
import java.util.concurrent.ThreadLocalRandom

object SimpleAi : Ai {
    override fun isApplicable(game: Game): Boolean {
        return !game.gameRules.smartAi
    }

    override fun determinePickAmount(game: Game): Int {
        return ThreadLocalRandom.current().nextInt(1, game.gameRules.maxItemsToTake + 1)
    }
}