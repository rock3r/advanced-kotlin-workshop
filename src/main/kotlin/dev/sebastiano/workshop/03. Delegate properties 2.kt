package dev.sebastiano.workshop

import dev.sebastiano.workshop.util.ValueProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0

class DelegateProperties2(private val valueProvider: ValueProvider<String>) {

    val fruit: String by cache { valueProvider.provideValue() }
}

private fun <T> cache(producer: () -> T): CacheableProperty<T> = CacheableProperty(producer)

private class CacheableProperty<out T>(val producer: () -> T) : ReadOnlyProperty<Any, T> {

    private var value = null // TODO

    override fun getValue(thisRef: Any, property: KProperty<*>): T = TODO()

    fun invalidate() {
        TODO()
    }
}

internal fun KProperty0<*>.invalidateCacheableProperty() {
    TODO()
}
