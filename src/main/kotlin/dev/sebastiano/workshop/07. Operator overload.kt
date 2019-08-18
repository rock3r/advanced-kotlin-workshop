package dev.sebastiano.workshop

data class HexNumber(val value: Int) : Comparable<HexNumber>, Number() {

    val hexString: String = TODO()

    operator fun plus(other: HexNumber): HexNumber = TODO()

    operator fun minus(other: HexNumber): HexNumber = TODO()

    operator fun div(other: HexNumber): HexNumber = TODO()

    operator fun times(other: HexNumber): HexNumber = TODO()

    operator fun get(position: Int): Char = TODO()

    operator fun set(position: Int, digit: Char): HexNumber = TODO()

    override fun compareTo(other: HexNumber): Int = TODO()

    override fun toByte(): Byte = TODO()

    override fun toChar(): Char = TODO()

    override fun toDouble(): Double = TODO()

    override fun toFloat(): Float = TODO()

    override fun toInt(): Int = TODO()

    override fun toLong(): Long = TODO()

    override fun toShort(): Short = TODO()

    companion object {

        private const val radix = 16

        fun from(hexString: String): HexNumber {
            val hasNegativeSign = hexString.startsWith("-")
            val hasPositiveSign = hexString.startsWith("+")
            val actualValue = if (hasNegativeSign || hasPositiveSign) hexString.substring(1) else hexString

            require(actualValue.all { it.isHexDigit }) { "The value '$hexString' is not a valid hex number" }
            return HexNumber(Integer.parseInt(hexString, radix))
        }

        private val Char.isHexDigit: Boolean
            get() = Character.digit(this, radix) != -1
    }
}
