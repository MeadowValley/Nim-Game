package de.meadowvalley.game.inputmodel

import kotlinx.serialization.Serializable

@Serializable
data class HumanInputWrapper(
    private val takeItemAmount: Int
) {
    fun getValidTakeItemAmount(maxItemsToTake: Int): Int {
        return if (takeItemAmount in 1..maxItemsToTake) takeItemAmount
        else if (takeItemAmount > maxItemsToTake) maxItemsToTake
        else 1
    }
}
