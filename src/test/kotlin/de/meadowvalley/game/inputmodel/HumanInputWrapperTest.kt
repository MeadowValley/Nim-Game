package de.meadowvalley.game.inputmodel

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class HumanInputWrapperTest {

    @Test
    fun `WHEN human input is valid THEN human input is returned`() {
        var wrapper = HumanInputWrapper(takeItemAmount = 3)
        var validInput = wrapper.getValidTakeItemAmount(maxItemsToTake = 3)
        assertEquals(3, validInput)

        wrapper = HumanInputWrapper(takeItemAmount = 2)
        validInput = wrapper.getValidTakeItemAmount(maxItemsToTake = 3)
        assertEquals(2, validInput)

        wrapper = HumanInputWrapper(takeItemAmount = 1)
        validInput = wrapper.getValidTakeItemAmount(maxItemsToTake = 3)
        assertEquals(1, validInput)
    }

    @Test
    fun `WHEN human input is too high THEN maxItemsToTake is returned`() {
        var wrapper = HumanInputWrapper(takeItemAmount = 4)
        var validInput = wrapper.getValidTakeItemAmount(maxItemsToTake = 3)
        assertEquals(3, validInput)

        wrapper = HumanInputWrapper(takeItemAmount = 100)
        validInput = wrapper.getValidTakeItemAmount(maxItemsToTake = 2)
        assertEquals(2, validInput)
    }

    @Test
    fun `WHEN human input is too low THEN 1 is returned`() {
        var wrapper = HumanInputWrapper(takeItemAmount = -5)
        var validInput = wrapper.getValidTakeItemAmount(maxItemsToTake = 3)
        assertEquals(1, validInput)

        wrapper = HumanInputWrapper(takeItemAmount = 0)
        validInput = wrapper.getValidTakeItemAmount(maxItemsToTake = 3)
        assertEquals(1, validInput)
    }
}