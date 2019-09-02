package dev.sebastiano.workshop

internal class LateInit {

    private lateinit var _fruit: String

    var fruit: String?
        get() = if (!::_fruit.isInitialized) null else _fruit
        set(value) {
            _fruit = value ?: throw IllegalArgumentException("The value must not be null")
        }

    fun performSomeWork(): String = "Here's something for ya: $_fruit"
}
