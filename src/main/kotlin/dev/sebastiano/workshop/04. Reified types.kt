package dev.sebastiano.workshop

inline fun <T, reified R : T> T.doIfTypeMatches(action: (T) -> Unit) {
    if (this is R) action(this)
}
