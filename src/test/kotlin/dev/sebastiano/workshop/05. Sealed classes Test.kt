package dev.sebastiano.workshop

import org.junit.jupiter.api.Test

internal class ThreeDimensionalShapeTest {

    private val originCoords = Coords(0, 0, 0)
    private val otherCoords1 = Coords(1, 1, 1)
    private val otherCoords2 = Coords(1, 2, 1)

    private val shapes = listOf<ThreeDimensionalShape>(
//            Point(originCoords),
//            Line(originCoords, otherCoords1),
//            Sphere(originCoords, 10),
//            ArbitraryShape("any-shape", listOf(originCoords, otherCoords1, otherCoords2))
    )

    @Test
    internal fun `should have a valid point`() {
        val shape = shapes[0]

        TODO("Need to uncomment test code")

//        assertThat(shape).isInstanceOf(Point::class)
//        shape as Point
//
//        assertThat(shape.name).isEqualTo("Point")
//        assertThat(shape.coords).isEqualTo(originCoords)
    }

    @Test
    internal fun `should have a valid line`() {
        val shape = shapes[1]

        TODO("Need to uncomment test code")

//        assertThat(shape).isInstanceOf(Line::class)
//        shape as Line
//
//        assertThat(shape.name).isEqualTo("Line")
//        assertThat(shape.startCoords).isEqualTo(originCoords)
//        assertThat(shape.endCoords).isEqualTo(otherCoords1)
    }

    @Test
    internal fun `should have a valid sphere`() {
        val shape = shapes[2]

        TODO("Need to uncomment test code")

//        assertThat(shape).isInstanceOf(Sphere::class)
//        shape as Sphere
//
//        assertThat(shape.name).isEqualTo("Sphere")
//        assertThat(shape.centerCoords).isEqualTo(originCoords)
//        assertThat(shape.radius).isEqualTo(10)
    }

    @Test
    internal fun `should have a valid arbitrary shape`() {
        val shape = shapes[3]

        TODO("Need to uncomment test code")

//        assertThat(shape).isInstanceOf(ArbitraryShape::class)
//        shape as ArbitraryShape
//
//        assertThat(shape.name).isEqualTo("any-shape")
//        assertThat(shape.coordinates).containsExactly(originCoords, otherCoords1, otherCoords2)
    }
}
