package dev.sebastiano.workshop

fun Iterable<Int>.product(): Int {
    require(count() > 0) { "The iterable must not be empty" }

    return reduce { product, factor -> Math.multiplyExact(product, factor) }
}

fun Iterable<Float>.division(): Float {
    require(count() > 0) { "The iterable must not be empty" }

    return reduce { division, divisor -> division / divisor }
}

fun Iterable<Int>.greatestCommonDivisor(): Int {
    require(count() > 1) { "The iterable must contain at least two numbers" }

    return map { it.toBigInteger() }
        .reduce { gcd, number -> gcd.gcd(number) }
        .toInt()
}
