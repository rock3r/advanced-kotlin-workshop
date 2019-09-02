package dev.sebastiano.workshop

data class HexNumber(val value: Int) : Comparable<HexNumber>, Number() {

    val hexString = value.toString(radix)

    operator fun plus(other: HexNumber): HexNumber = HexNumber(Math.addExact(value, other.value))

    operator fun minus(other: HexNumber): HexNumber = HexNumber(Math.subtractExact(value, other.value))

    operator fun div(other: HexNumber): HexNumber {
        if (other.value == 0) throw ArithmeticException("Dividing by zero")
        return HexNumber(value / other.value)
    }

    operator fun times(other: HexNumber): HexNumber = HexNumber(Math.multiplyExact(value, other.value))

    operator fun get(position: Int): Char = hexString[position]

    operator fun set(position: Int, digit: Char): HexNumber {
        require(digit.isHexDigit) { "The character '$digit' is not a valid hex digit" }
        return from(hexString.replaceAt(position, digit))
    }

    private fun String.replaceAt(position: Int, char: Char) = buildString {
        append(this@replaceAt.substring(0 until position))
        append(char)
        append(this@replaceAt.substring(position + 1))
    }

    override fun compareTo(other: HexNumber): Int = (this - other).value.coerceIn(-1..1)

    override fun toByte(): Byte = value.toByte()

    override fun toChar(): Char = value.toChar()

    override fun toDouble(): Double = value.toDouble()

    override fun toFloat(): Float = value.toFloat()

    override fun toInt(): Int = value

    override fun toLong(): Long = value.toLong()

    override fun toShort(): Short = value.toShort()

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
