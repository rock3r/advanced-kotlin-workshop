package dev.sebastiano.workshop

import dev.sebastiano.workshop.util.ValueProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.isAccessible

class DelegateProperties2(private val valueProvider: ValueProvider<String>) {

    val fruit: String by cache { valueProvider.provideValue() }
}

private fun <T> cache(producer: () -> T): CacheableProperty<T> = CacheableProperty(producer)

private class CacheableProperty<out T>(val producer: () -> T) : ReadOnlyProperty<Any, T> {

    private var value: CachedValue<T> = CachedValue.Nothing

    override fun getValue(thisRef: Any, property: KProperty<*>): T =
        when (value) {
            is CachedValue.Nothing -> producer().also { value = CachedValue.Something(it) }
            is CachedValue.Something<T> -> (value as CachedValue.Something<T>).value
        }

    fun invalidate() {
        value = CachedValue.Nothing
    }

    private sealed class CachedValue<out T> {
        class Something<out T>(val value: T) : CachedValue<T>()
        object Nothing : CachedValue<kotlin.Nothing>()
    }
}

internal fun KProperty0<*>.invalidateCacheableProperty() {
    val wasAccessible = isAccessible
    isAccessible = true

    if (getDelegate() !is CacheableProperty<*>) {
        throw UnsupportedOperationException("Only CacheableProperty supports invalidation")
    }
    (getDelegate() as CacheableProperty<*>).invalidate()

    isAccessible = wasAccessible
}
