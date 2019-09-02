package dev.sebastiano.workshop

import dev.sebastiano.workshop.util.ValueProvider

class DelegateProperties1(private val valueProvider: ValueProvider<String>) {

    val fruit: String by lazy { valueProvider.provideValue() }
}
