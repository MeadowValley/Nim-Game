package de.meadowvalley.game.model

import kotlinx.serialization.Serializable

/**
 * These are the game rules.
 */
@Serializable
data class GameRules(
    val smartAi: Boolean = true,
    val maxItemsToTake: Int = 3,
    val itemCountOnStart: Int = 13,
)