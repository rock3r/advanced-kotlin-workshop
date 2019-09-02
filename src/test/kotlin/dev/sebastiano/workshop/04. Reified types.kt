package dev.sebastiano.workshop

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import dev.sebastiano.workshop.ReifiedTypesTest.Fruit.Apple
import dev.sebastiano.workshop.ReifiedTypesTest.Fruit.Banana
import dev.sebastiano.workshop.ReifiedTypesTest.Fruit.Melon
import dev.sebastiano.workshop.ReifiedTypesTest.Fruit.Orange
import org.junit.jupiter.api.Test

internal class ReifiedTypesTest {

    private val items = setOf(Banana, Apple, Orange)

    @Test
    internal fun `should execute the action when the type matches`() {
        var value: Fruit? = null
        items.forEach { item ->
            item.doIfTypeMatches<Fruit, Banana> { fruit -> value = fruit }
        }

        assertThat(value).isEqualTo(Banana)
    }

    @Test
    internal fun `should never execute the action when the type doesn't match`() {
        var value: Fruit? = null
        items.forEach { item ->
            item.doIfTypeMatches<Fruit, Melon> { fruit -> value = fruit }
        }

        assertThat(value).isNull()
    }

    sealed class Fruit {
        object Banana : Fruit()
        object Apple : Fruit()
        object Orange : Fruit()
        object Melon : Fruit()
    }
}
