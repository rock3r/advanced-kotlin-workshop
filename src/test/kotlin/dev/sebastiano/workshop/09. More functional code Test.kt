package dev.sebastiano.workshop

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import assertk.assertions.isZero
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class FunctionalMathsTest {

    @Nested
    inner class Product {

        @Test
        internal fun `should throw IllegalArgumentException when the iterable is empty`() {
            assertThat { emptyList<Int>().product() }.isFailure()
                    .isInstanceOf(IllegalArgumentException::class)
        }

        @Test
        internal fun `should return the value itself when the iterable only contains one number`() {
            assertThat(listOf(42).product()).isEqualTo(42)
        }

        @Test
        internal fun `should throw ArithmeticException when the product overflow or underflows int`() {
            assertAll {
                assertThat { listOf(Int.MIN_VALUE, 2).product() }.isFailure()
                        .isInstanceOf(ArithmeticException::class)
                assertThat { listOf(Int.MAX_VALUE, 2).product() }.isFailure()
                        .isInstanceOf(ArithmeticException::class)
            }
        }

        @Test
        internal fun `should return 0 when the iterable contains more than one number, at least one of which is 0`() {
            assertAll {
                assertThat(listOf(100, 100, 0).product()).isZero()
                assertThat(listOf(100, 0, 0).product()).isZero()
                assertThat(listOf(0, 0).product()).isZero()
            }
        }

        @Test
        internal fun `should return the non-1 value when the iterable contains two numbers, one of which is 1`() {
            assertThat(listOf(75, 1).product()).isEqualTo(75)
        }

        @Test
        internal fun `should return the correct product value when the iterable contains more than one number`() {
            val twoItems = listOf(15, 75)
            val multipleItems = listOf(20, 32, 8, 12, 16)
            assertAll {
                assertThat(twoItems.product()).isEqualTo(1_125)
                assertThat(multipleItems.product()).isEqualTo(983_040)
            }
        }
    }

    @Nested
    inner class Division {

        @Test
        internal fun `should throw IllegalArgumentException when the iterable is empty`() {
            assertThat { emptyList<Float>().division() }.isFailure()
                    .isInstanceOf(IllegalArgumentException::class)
        }

        @Test
        internal fun `should return the value itself when the iterable only contains one number`() {
            assertThat(listOf(42f).division()).isEqualTo(42f)
        }

        @Test
        internal fun `should return Infinity when one or more numbers in the iterable after the first is zero`() {
            assertAll {
                assertThat(listOf(10f, 0f).division()).isEqualTo(Float.POSITIVE_INFINITY)
                assertThat(listOf(-10f, 0f).division()).isEqualTo(Float.NEGATIVE_INFINITY)
                assertThat(listOf(10f, 2f, 0f, 0f, 2f).division()).isEqualTo(Float.POSITIVE_INFINITY)
                assertThat(listOf(10f, 0f, 0f, 2f).division()).isEqualTo(Float.POSITIVE_INFINITY)
                assertThat(listOf(-10f, 0f, 0f, 2f).division()).isEqualTo(Float.NEGATIVE_INFINITY)
            }
        }

        @Test
        internal fun `should return NaN when all numbers in the iterable are zero`() {
            assertAll {
                assertThat(listOf(0f, 0f).division()).isEqualTo(Float.NaN)
                assertThat(listOf(-0f, 0f).division()).isEqualTo(Float.NaN)
                assertThat(listOf(0f, 0f, 0f, 0f, 0f).division()).isEqualTo(Float.NaN)
            }
        }

        @Test
        internal fun `should return 0 when only the first number in the iterable is zero`() {
            assertAll {
                assertThat(listOf(0f, 10f).division()).isZero()
                assertThat(listOf(-0f, 10f).division()).isZero()
                assertThat(listOf(0f, 10_000f, 10f, 10f, 10f).division()).isZero()
            }
        }

        @Test
        internal fun `should return the non-1 value when the iterable contains two numbers, the latter of which is 1`() {
            assertThat(listOf(75f, 1f).division()).isEqualTo(75f)
        }

        @Test
        internal fun `should return the correct division value when the iterable contains more than one number`() {
            val twoItems = listOf(15f, 7f)
            val multipleItems = listOf(2000032f, 32f, 8f, 12f, 16f)
            assertAll {
                assertThat(twoItems.division()).isEqualTo(2.142857f)
                assertThat(multipleItems.division()).isEqualTo(40.690754f)
            }
        }
    }

    @Nested
    inner class GreatestCommonDivisor {

        @Test
        internal fun `should throw IllegalArgumentException when the iterable is empty`() {
            assertThat { emptyList<Int>().greatestCommonDivisor() }.isFailure()
                    .isInstanceOf(IllegalArgumentException::class)
        }

        @Test
        internal fun `should throw IllegalArgumentException when the iterable contains less than two numbers`() {
            assertThat { listOf(1).greatestCommonDivisor() }.isFailure()
                    .isInstanceOf(IllegalArgumentException::class)
        }

        @Test
        internal fun `should return zero if the numbers are two or more zeroes`() {
            val twoItems = List(2) { 0 }
            val threeItems = List(3) { 0 }
            assertAll {
                assertThat(twoItems.greatestCommonDivisor()).isZero()
                assertThat(threeItems.greatestCommonDivisor()).isZero()
            }
        }

        @Test
        internal fun `should return the correct GCD for two or more numbers`() {
            val twoItems = listOf(15, 75)
            val multipleItems = listOf(20, 32, 8, 12, 16)
            assertAll {
                assertThat(twoItems.greatestCommonDivisor()).isEqualTo(15)
                assertThat(multipleItems.greatestCommonDivisor()).isEqualTo(4)
            }
        }
    }
}
