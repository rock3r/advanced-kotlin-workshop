package dev.sebastiano.workshop

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import assertk.assertions.isNull
import org.junit.jupiter.api.Test

internal class LateInitTest {

    @Test
    internal fun `should return null when accessing fruit before it's initialized`() {
        val lateInit = LateInit()

        assertThat(lateInit.fruit).isNull()
    }

    @Test
    internal fun `should throw IllegalArgumentException when initializing fruit with null`() {
        val lateInit = LateInit()

        assertThat { lateInit.fruit = null }.isFailure()
                .isInstanceOf(IllegalArgumentException::class)
    }

    @Test
    internal fun `should return the correct value when accessing fruit after it's initialized`() {
        val lateInit = LateInit()
        lateInit.fruit = "banana"

        assertThat(lateInit.fruit).isEqualTo("banana")
    }

    @Test
    internal fun `should throw UninitializedPropertyAccessException when calling performSomeWork() before fruit is initialized`() {
        val lateInit = LateInit()

        assertThat { lateInit.performSomeWork() }.isFailure()
                .isInstanceOf(UninitializedPropertyAccessException::class)
    }

    @Test
    internal fun `should return the correct interpolated string when calling performSomeWork() after fruit is initialized`() {
        val lateInit = LateInit()
        lateInit.fruit = "banana"

        assertThat(lateInit.performSomeWork()).isEqualTo("Here's something for ya: banana")
    }
}
