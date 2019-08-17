package dev.sebastiano.workshop.util

interface ValueProvider<T> {

    fun provideValue(): T
}
