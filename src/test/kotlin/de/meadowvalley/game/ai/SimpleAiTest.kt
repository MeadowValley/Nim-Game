package de.meadowvalley.game.ai

import de.meadowvalley.game.model.Game
import de.meadowvalley.game.model.GameRules
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertContains

internal class SimpleAiTest {
    @Test
    fun `WHEN smartAi is false THEN simpleAi is applicable`() {
        val game = Game(gameRules = GameRules(smartAi = false))
        assertEquals(true, SimpleAi.isApplicable(game))
    }

    @Test
    fun `WHEN smartAi is true THEN simpleAi is not applicable`() {
        val game = Game(gameRules = GameRules(smartAi = true))
        assertEquals(false, SimpleAi.isApplicable(game))
    }

    @Test
    fun `WHEN simpleAi determines pick amount THEN the result is not bigger than maxItemsToTake`() {
        val testAmount = 500
        val game = Game(gameRules = GameRules(smartAi = false, maxItemsToTake = 2))
        for (i in 0..testAmount) {
            assertContains(listOf(1, 2), SimpleAi.determinePickAmount(game))
        }
    }
}