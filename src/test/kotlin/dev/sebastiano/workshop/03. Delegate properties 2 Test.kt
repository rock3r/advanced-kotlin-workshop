package dev.sebastiano.workshop

import assertk.assertThat
import dev.sebastiano.workshop.util.TestValueProvider
import dev.sebastiano.workshop.util.hasAccessCount
import dev.sebastiano.workshop.util.hasBeenAccessedOnce
import dev.sebastiano.workshop.util.hasNotBeenAccessed
import org.junit.jupiter.api.Test

internal class DelegateProperties2Test {

    private val valueProvider = TestValueProvider("banana")

    @Test
    internal fun `should not obtain the value from the ValueProvider when fruit is never accessed`() {
        DelegateProperties2(valueProvider)

        assertThat(valueProvider).hasNotBeenAccessed()
    }

    @Test
    internal fun `should not obtain the value from the ValueProvider when fruit is never accessed, even when invalidated`() {
        DelegateProperties2(valueProvider).apply {
            ::fruit.invalidateCacheableProperty()
        }

        assertThat(valueProvider).hasNotBeenAccessed()
    }

    @Test
    internal fun `should obtain the value from the ValueProvider once when fruit is accessed once`() {
        DelegateProperties2(valueProvider).apply {
            println("Accessing fruit value: $fruit")
        }

        assertThat(valueProvider).hasBeenAccessedOnce()
    }

    @Test
    internal fun `should obtain the value from the ValueProvider once when fruit is invalidated, and then accessed once`() {
        DelegateProperties2(valueProvider).apply {
            ::fruit.invalidateCacheableProperty()
            println("Accessing fruit value: $fruit")
        }

        assertThat(valueProvider).hasBeenAccessedOnce()
    }

    @Test
    internal fun `should obtain the value from the ValueProvider once when fruit is invalidated multiple times, and then accessed once`() {
        DelegateProperties2(valueProvider).apply {
            ::fruit.invalidateCacheableProperty()
            ::fruit.invalidateCacheableProperty()
            println("Accessing fruit value: $fruit")
        }

        assertThat(valueProvider).hasBeenAccessedOnce()
    }

    @Test
    internal fun `should obtain the value from the ValueProvider once when fruit is accessed multiple times and never invalidated`() {
        DelegateProperties2(valueProvider).apply {
            println("Accessing fruit value: $fruit")
            println("Accessing fruit value: $fruit")
            println("Accessing fruit value: $fruit")
        }

        assertThat(valueProvider).hasBeenAccessedOnce()
    }

    @Test
    internal fun `should obtain the value from the ValueProvider twice when fruit is accessed twice, and then twice again after being invalidated`() {
        DelegateProperties2(valueProvider).apply {
            println("Accessing fruit value: $fruit")
            println("Accessing fruit value: $fruit")
            ::fruit.invalidateCacheableProperty()
            println("Accessing fruit value: $fruit")
            println("Accessing fruit value: $fruit")
        }

        assertThat(valueProvider).hasAccessCount(2)
    }

    @Test
    internal fun `should obtain the value from the ValueProvider twice when fruit is accessed twice, and then twice again after being invalidated twice in a row`() {
        DelegateProperties2(valueProvider).apply {
            println("Accessing fruit value: $fruit")
            println("Accessing fruit value: $fruit")
            ::fruit.invalidateCacheableProperty()
            ::fruit.invalidateCacheableProperty()
            println("Accessing fruit value: $fruit")
            println("Accessing fruit value: $fruit")
        }

        assertThat(valueProvider).hasAccessCount(2)
    }
}
