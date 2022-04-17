package dev.sebastiano.workshop

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import assertk.assertions.isZero
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class HexNumberTest {

    @Nested
    inner class FromHexString {

        @Test
        internal fun `should throw IllegalArgumentException when created from a non-hex string`() {
            assertThat { HexNumber.from("banana") }.isFailure()
                .isInstanceOf(IllegalArgumentException::class)
        }

        @Test
        internal fun `should throw IllegalArgumentException when initialized with an empty string`() {
            assertThat { HexNumber.from("") }.isFailure()
                .isInstanceOf(IllegalArgumentException::class)
        }

        @Test
        internal fun `should throw NumberFormatException when initialized with a value larger than Int's MAX_VALUE`() {
            assertThat { HexNumber.from("80000000") }.isFailure()
                .isInstanceOf(NumberFormatException::class)
        }

        @Test
        internal fun `should throw NumberFormatException when initialized with a value smaller than Int's MIN_VALUE`() {
            assertThat { HexNumber.from("-80000001") }.isFailure()
                .isInstanceOf(NumberFormatException::class)
        }

        @Test
        internal fun `should hold the correct positive integer representation in the intValue property`() {
            assertThat(HexNumber.from("0").value).isZero()
            assertThat(HexNumber.from("+1").value).isEqualTo(1)
            assertThat(HexNumber.from("01").value).isEqualTo(1)
            assertThat(HexNumber.from("+10").value).isEqualTo(16)
            assertThat(HexNumber.from("ffff").value).isEqualTo(65535)
            assertThat(HexNumber.from("7fffffff").value).isEqualTo(Int.MAX_VALUE)
        }

        @Test
        internal fun `should hold the correct negative integer representation in the intValue property`() {
            assertThat(HexNumber.from("-1").value).isEqualTo(-1)
            assertThat(HexNumber.from("-01").value).isEqualTo(-1)
            assertThat(HexNumber.from("-10").value).isEqualTo(-16)
            assertThat(HexNumber.from("-ffff").value).isEqualTo(-65535)
            assertThat(HexNumber.from("-80000000").value).isEqualTo(Int.MIN_VALUE)
        }
    }

    @Nested
    inner class HexString {

        @Test
        internal fun `should represent the int value as the correct hex string`() {
            assertThat(HexNumber(0x0).hexString).isEqualTo("0")
            assertThat(HexNumber(0x1).hexString).isEqualTo("1")
            assertThat(HexNumber(0x10).hexString).isEqualTo("10")
            assertThat(HexNumber(-0x0).hexString).isEqualTo("0")
            assertThat(HexNumber(-0x1).hexString).isEqualTo("-1")
            assertThat(HexNumber(-0x10).hexString).isEqualTo("-10")
        }
    }

    @Nested
    inner class Plus {

        @Test
        internal fun `should throw ArithmeticException when the sum is a value larger than Int's MAX_VALUE`() {
            assertThat { HexNumber(0x1) + HexNumber(0x7fffffff) }.isFailure()
                .isInstanceOf(ArithmeticException::class)
        }

        @Test
        internal fun `should throw ArithmeticException when the sum is a value smaller than Int's MIN_VALUE`() {
            assertThat { HexNumber(-0x1) + HexNumber(-0x80000000) }.isFailure()
                .isInstanceOf(ArithmeticException::class)
        }

        @Test
        internal fun `should add two positive numbers correctly`() {
            assertThat(HexNumber(0x1) + HexNumber(0x2)).isEqualTo(HexNumber(0x3))
            assertThat(HexNumber(0x1) + HexNumber(0xf)).isEqualTo(HexNumber(0x10))
        }

        @Test
        internal fun `should add two negative numbers correctly`() {
            assertThat(HexNumber(-0x1) + HexNumber(-0x2)).isEqualTo(HexNumber(-0x3))
            assertThat(HexNumber(-0x1) + HexNumber(-0xf)).isEqualTo(HexNumber(-0x10))
        }

        @Test
        internal fun `should add zero and a positive number correctly`() {
            assertThat(HexNumber(0x0) + HexNumber(0x2)).isEqualTo(HexNumber(0x2))
            assertThat(HexNumber(0xff) + HexNumber(0x0)).isEqualTo(HexNumber(0xff))
        }

        @Test
        internal fun `should add zero and a negative number correctly`() {
            assertThat(HexNumber(0x0) + HexNumber(-0x2)).isEqualTo(HexNumber(-0x2))
            assertThat(HexNumber(-0xff) + HexNumber(0x0)).isEqualTo(HexNumber(-0xff))
        }

        @Test
        internal fun `should add a positive and a negative number correctly`() {
            assertThat(HexNumber(0x1) + HexNumber(-0x2)).isEqualTo(HexNumber(-0x1))
            assertThat(HexNumber(-0x1) + HexNumber(0xf)).isEqualTo(HexNumber(0xe))
            assertThat(HexNumber(-0x80000000) + HexNumber(0x7fffffff)).isEqualTo(HexNumber(-0x1))
        }
    }

    @Nested
    inner class Minus {

        @Test
        internal fun `should throw ArithmeticException when the difference is a value larger than Int's MAX_VALUE`() {
            assertThat { HexNumber(0x1) - HexNumber(-0x7fffffff) }.isFailure()
                .isInstanceOf(ArithmeticException::class)
        }

        @Test
        internal fun `should throw ArithmeticException when the difference is a value smaller than Int's MIN_VALUE`() {
            assertThat { HexNumber(-0x80000000) - HexNumber(0x1) }.isFailure()
                .isInstanceOf(ArithmeticException::class)
        }

        @Test
        internal fun `should subtract two positive numbers correctly`() {
            assertThat(HexNumber(0x3) - HexNumber(0x2)).isEqualTo(HexNumber(0x1))
            assertThat(HexNumber(0x1) - HexNumber(0x2)).isEqualTo(HexNumber(-0x1))
            assertThat(HexNumber(0x2) - HexNumber(0x11)).isEqualTo(HexNumber(-0xf))
            assertThat(HexNumber(0x1) - HexNumber(0x11)).isEqualTo(HexNumber(-0x10))
        }

        @Test
        internal fun `should subtract two negative numbers correctly`() {
            assertThat(HexNumber(-0x1) - HexNumber(-0x2)).isEqualTo(HexNumber(0x1))
            assertThat(HexNumber(-0x1) - HexNumber(-0xf)).isEqualTo(HexNumber(0xe))
        }

        @Test
        internal fun `should subtract zero and a positive number correctly`() {
            assertThat(HexNumber(0x0) - HexNumber(0x2)).isEqualTo(HexNumber(-0x2))
            assertThat(HexNumber(0xff) - HexNumber(0x0)).isEqualTo(HexNumber(0xff))
        }

        @Test
        internal fun `should subtract zero and a negative number correctly`() {
            assertThat(HexNumber(0x0) - HexNumber(-0x2)).isEqualTo(HexNumber(0x2))
            assertThat(HexNumber(-0xff) - HexNumber(0x0)).isEqualTo(HexNumber(-0xff))
        }

        @Test
        internal fun `should subtract a positive and a negative number correctly`() {
            assertThat(HexNumber(0x1) - HexNumber(-0x2)).isEqualTo(HexNumber(0x3))
            assertThat(HexNumber(-0x1) - HexNumber(0xf)).isEqualTo(HexNumber(-0x10))
        }
    }

    @Nested
    inner class Div {

        @Test
        internal fun `should divide two positive numbers correctly`() {
            assertThat(HexNumber(0x4) / HexNumber(0x2)).isEqualTo(HexNumber(0x2))
            assertThat(HexNumber(0x1) / HexNumber(0x2)).isEqualTo(HexNumber(0x0))
            assertThat(HexNumber(0xFF) / HexNumber(0xF)).isEqualTo(HexNumber(0x11))
        }

        @Test
        internal fun `should divide two negative numbers correctly`() {
            assertThat(HexNumber(-0x4) / HexNumber(-0x2)).isEqualTo(HexNumber(0x2))
            assertThat(HexNumber(-0x6) / HexNumber(-0x5)).isEqualTo(HexNumber(0x1))
            assertThat(HexNumber(-0xf) / HexNumber(-0x2)).isEqualTo(HexNumber(0x7))
            assertThat(HexNumber(-0x1) / HexNumber(-0xf)).isEqualTo(HexNumber(0x0))
        }

        @Test
        internal fun `should divide zero by a positive number correctly`() {
            assertThat(HexNumber(0x0) / HexNumber(0x2)).isEqualTo(HexNumber(0x0))
        }

        @Test
        internal fun `should divide zero by a negative number correctly`() {
            assertThat(HexNumber(0x0) / HexNumber(-0x2)).isEqualTo(HexNumber(0x0))
        }

        @Test
        internal fun `should throw ArithmeticException when dividing any number by zero`() {
            assertThat { HexNumber(0x2) / HexNumber(0x0) }.isFailure().isInstanceOf(ArithmeticException::class)
            assertThat { HexNumber(-0x1) / HexNumber(0x0) }.isFailure().isInstanceOf(ArithmeticException::class)
            assertThat { HexNumber(0x0) / HexNumber(0x0) }.isFailure().isInstanceOf(ArithmeticException::class)
        }

        @Test
        internal fun `should divide a positive and a negative number correctly`() {
            assertThat(HexNumber(0x10) / HexNumber(-0x3)).isEqualTo(HexNumber(-0x5))
            assertThat(HexNumber(0x2) / HexNumber(-0x3)).isEqualTo(HexNumber(0x0))
            assertThat(HexNumber(-0xf) / HexNumber(0xf)).isEqualTo(HexNumber(-0x1))
            assertThat(HexNumber(-0x12) / HexNumber(0x4)).isEqualTo(HexNumber(-0x4))
        }
    }

    @Nested
    inner class Times {

        @Test
        internal fun `should throw ArithmeticException when the product is a value larger than Int's MAX_VALUE`() {
            assertThat { HexNumber(0x7fffffff) * HexNumber(0x2) }.isFailure()
                .isInstanceOf(ArithmeticException::class)
        }

        @Test
        internal fun `should throw ArithmeticException when the product is a value smaller than Int's MIN_VALUE`() {
            assertThat { HexNumber(-0x80000000) * HexNumber(0x2) }.isFailure()
                .isInstanceOf(ArithmeticException::class)
        }

        @Test
        internal fun `should multiply two positive numbers correctly`() {
            assertThat(HexNumber(0x4) * HexNumber(0x2)).isEqualTo(HexNumber(0x8))
            assertThat(HexNumber(0x1) * HexNumber(0x2)).isEqualTo(HexNumber(0x2))
            assertThat(HexNumber(0xFF) * HexNumber(0xF)).isEqualTo(HexNumber(0xef1))
        }

        @Test
        internal fun `should multiply two negative numbers correctly`() {
            assertThat(HexNumber(-0x4) * HexNumber(-0x2)).isEqualTo(HexNumber(0x8))
            assertThat(HexNumber(-0x6) * HexNumber(-0x5)).isEqualTo(HexNumber(0x1e))
            assertThat(HexNumber(-0xf) * HexNumber(-0x2)).isEqualTo(HexNumber(0x1e))
            assertThat(HexNumber(-0x1) * HexNumber(-0xf)).isEqualTo(HexNumber(0xf))
        }

        @Test
        internal fun `should multiply zero by a positive number correctly`() {
            assertThat(HexNumber(0x0) * HexNumber(0x2)).isEqualTo(HexNumber(0x0))
        }

        @Test
        internal fun `should multiply zero by a negative number correctly`() {
            assertThat(HexNumber(0x0) * HexNumber(-0x2)).isEqualTo(HexNumber(0x0))
        }

        @Test
        internal fun `should multiply a positive and a negative number correctly`() {
            assertThat(HexNumber(0x10) * HexNumber(-0x3)).isEqualTo(HexNumber(-0x30))
            assertThat(HexNumber(0x2) * HexNumber(-0x3)).isEqualTo(HexNumber(-0x6))
            assertThat(HexNumber(-0xf) * HexNumber(0xf)).isEqualTo(HexNumber(-0xe1))
            assertThat(HexNumber(-0x12) * HexNumber(0x4)).isEqualTo(HexNumber(-0x48))
        }
    }

    @Nested
    inner class Get {

        @Test
        internal fun `should throw IndexOutOfBounds if the index is negative`() {
            assertThat { HexNumber(0x123456F)[-1] }.isFailure()
                .isInstanceOf(IndexOutOfBoundsException::class)
        }

        @Test
        internal fun `should throw IndexOutOfBounds if the index is equal to the hex string length`() {
            assertThat { HexNumber(0x123456F)[7] }.isFailure()
                .isInstanceOf(IndexOutOfBoundsException::class)
        }

        @Test
        internal fun `should throw IndexOutOfBounds if the index is greater than the hex string length`() {
            assertThat { HexNumber(0x123456F)[8] }.isFailure()
                .isInstanceOf(IndexOutOfBoundsException::class)
        }

        @Test
        internal fun `should return the correct digit if the index is in the valid range`() {
            assertThat(HexNumber(0x123456F)[2]).isEqualTo('3')
        }
    }

    @Nested
    inner class Set {

        @Test
        internal fun `should throw IndexOutOfBounds if the index is negative`() {
            assertThat { HexNumber(0x123456F)[-1] = '1' }.isFailure()
                .isInstanceOf(IndexOutOfBoundsException::class)
        }

        @Test
        internal fun `should throw IndexOutOfBounds if the index is equal to the hex string length`() {
            assertThat { HexNumber(0x123456F)[7] = '1' }.isFailure()
                .isInstanceOf(IndexOutOfBoundsException::class)
        }

        @Test
        internal fun `should throw IndexOutOfBounds if the index is greater than the hex string length`() {
            assertThat { HexNumber(0x123456F)[8] = '1' }.isFailure()
                .isInstanceOf(IndexOutOfBoundsException::class)
        }

        @Test
        internal fun `should return the correct digit if the index is in the valid range`() {
            assertThat(HexNumber(0x123456F).set(2, '1')).isEqualTo(HexNumber(0x121456F))
        }
    }

    @Nested
    inner class NumberOverrides {

        @Test
        internal fun `should convert the value to other Numbers correctly`() {
            assertThat(HexNumber(0x32).toByte()).isEqualTo(50.toByte())
            assertThat(HexNumber(0x32).toChar()).isEqualTo('2')
            assertThat(HexNumber(0x32).toDouble()).isEqualTo(50.0)
            assertThat(HexNumber(0x32).toFloat()).isEqualTo(50F)
            assertThat(HexNumber(0x32).toInt()).isEqualTo(50)
            assertThat(HexNumber(0x32).toLong()).isEqualTo(50L)
            assertThat(HexNumber(0x32).toShort()).isEqualTo(50.toShort())
        }
    }
}
