package dev.sebastiano.workshop

internal class LateInit {

    private lateinit var _fruit: String

    var fruit: String?
        get() = TODO()
        set(value) = TODO()

    fun performSomeWork(): String = "Here's something for ya: $_fruit"
}
