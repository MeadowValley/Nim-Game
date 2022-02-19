package de.meadowvalley.game.ai

import de.meadowvalley.game.model.Game
import de.meadowvalley.game.model.GameRules
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class SmartAiTest {
    @Test
    fun `WHEN smartAi is true THEN smartAi is applicable`() {
        val game = Game(gameRules = GameRules(smartAi = false))
        assertEquals(false, SmartAi.isApplicable(game))
    }

    @Test
    fun `WHEN smartAi is false THEN smartAi is not applicable`() {
        val game = Game(gameRules = GameRules(smartAi = true))
        assertEquals(true, SmartAi.isApplicable(game))
    }

    @Test
    fun `WHEN maxItemsToTake=3 THEN pick result is optimal`() {
        val maxItemsToTake = 3
        val testAmount = 100

        val numbers = mutableMapOf<Int, MutableList<Int>>()
        for (i in 1..maxItemsToTake) numbers[i] = mutableListOf()

        for (i in 1..testAmount) {
            when (i.rem(maxItemsToTake + 1)) {
                1, 2 -> numbers[1]?.add(i)
                3 -> numbers[2]?.add(i)
                0 -> numbers[3]?.add(i)
            }
        }

        var gameRules: GameRules
        var bestPick: Int

        for (expectedValueMap in numbers) {
            val numberOfItemsList = expectedValueMap.value
            for (itemCountOnStart in numberOfItemsList) {
                gameRules =
                    GameRules(smartAi = true, maxItemsToTake = maxItemsToTake, itemCountOnStart = itemCountOnStart)
                bestPick = SmartAi.determinePickAmount(Game(gameRules = gameRules))
                assertEquals(expectedValueMap.key, bestPick, "itemCountOnStart=$itemCountOnStart")
            }
        }
    }

    @Test
    fun `WHEN maxItemsToTake=5 THEN pick result is optimal`() {
        val maxItemsToTake = 5
        val testAmount = 100

        val numbers = mutableMapOf<Int, MutableList<Int>>()
        for (i in 1..maxItemsToTake) numbers[i] = mutableListOf()

        for (i in 1..testAmount) {
            when (i.rem(maxItemsToTake + 1)) {
                1, 2 -> numbers[1]?.add(i)
                3 -> numbers[2]?.add(i)
                4 -> numbers[3]?.add(i)
                5 -> numbers[4]?.add(i)
                0 -> numbers[5]?.add(i)
            }
        }

        var gameRules: GameRules
        var bestPick: Int

        for (expectedValueMap in numbers) {
            val numberOfItemsList = expectedValueMap.value
            for (itemCountOnStart in numberOfItemsList) {
                gameRules =
                    GameRules(smartAi = true, maxItemsToTake = maxItemsToTake, itemCountOnStart = itemCountOnStart)
                bestPick = SmartAi.determinePickAmount(Game(gameRules = gameRules))
                assertEquals(expectedValueMap.key, bestPick, "itemCountOnStart=$itemCountOnStart")
            }
        }
    }
}