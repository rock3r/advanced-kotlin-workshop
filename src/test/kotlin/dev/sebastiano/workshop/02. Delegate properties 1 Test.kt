package dev.sebastiano.workshop

import assertk.assertThat
import dev.sebastiano.workshop.util.TestValueProvider
import dev.sebastiano.workshop.util.hasBeenAccessedOnce
import dev.sebastiano.workshop.util.hasNotBeenAccessed
import org.junit.jupiter.api.Test

internal class DelegateProperties1Test {

    private val valueProvider = TestValueProvider("banana")

    @Test
    internal fun `should not obtain the value from the ValueProvider when fruit is never accessed`() {
        DelegateProperties1(valueProvider)

        assertThat(valueProvider).hasNotBeenAccessed()
    }

    @Test
    internal fun `should obtain the value from the ValueProvider once when fruit is accessed once`() {
        DelegateProperties1(valueProvider).apply {
            println("Accessing fruit value: $fruit")
        }

        assertThat(valueProvider).hasBeenAccessedOnce()
    }

    @Test
    internal fun `should obtain the value from the ValueProvider once when fruit is accessed multiple times`() {
        DelegateProperties1(valueProvider).apply {
            println("Accessing fruit value: $fruit")
            println("Accessing fruit value: $fruit")
            println("Accessing fruit value: $fruit")
        }

        assertThat(valueProvider).hasBeenAccessedOnce()
    }
}
