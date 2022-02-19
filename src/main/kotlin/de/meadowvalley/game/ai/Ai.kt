package de.meadowvalley.game.ai

import de.meadowvalley.game.model.Game

interface Ai {
    /**
     * This function will decide if a specific AI is applicable for a game.
     */
    fun isApplicable(game: Game): Boolean

    /**
     * This function will return the number of items the AI wants to pick.
     */
    fun determinePickAmount(game: Game): Int
}