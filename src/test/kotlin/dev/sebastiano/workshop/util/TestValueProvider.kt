package dev.sebastiano.workshop.util

import assertk.Assert
import assertk.assertions.isEqualTo

internal class TestValueProvider<T>(private val value: T) : ValueProvider<T> {

    var accessCount = 0

    override fun provideValue(): T {
        accessCount++
        return value
    }
}

internal fun <T> Assert<TestValueProvider<T>>.hasNotBeenAccessed() = hasAccessCount(0)

internal fun <T> Assert<TestValueProvider<T>>.hasBeenAccessedOnce() = hasAccessCount(1)

internal fun <T> Assert<TestValueProvider<T>>.hasAccessCount(expected: Int) {
    given { assertThat(it.accessCount).isEqualTo(expected) }
}
