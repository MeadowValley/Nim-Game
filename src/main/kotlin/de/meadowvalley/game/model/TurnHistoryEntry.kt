package de.meadowvalley.game.model

import kotlinx.serialization.Serializable

@Serializable
data class TurnHistoryEntry(
    val player: Player,
    val itemsTaken: Int,
    val itemCount: Int,
)
