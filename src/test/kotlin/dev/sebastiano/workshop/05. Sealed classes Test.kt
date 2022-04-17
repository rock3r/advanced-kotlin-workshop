package dev.sebastiano.workshop

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import dev.sebastiano.workshop.ThreeDimensionalShape.ArbitraryShape
import dev.sebastiano.workshop.ThreeDimensionalShape.Line
import dev.sebastiano.workshop.ThreeDimensionalShape.Point
import dev.sebastiano.workshop.ThreeDimensionalShape.Sphere
import org.junit.jupiter.api.Test

internal class ThreeDimensionalShapeTest {

    private val originCoords = Coords(0, 0, 0)
    private val otherCoords1 = Coords(1, 1, 1)
    private val otherCoords2 = Coords(1, 2, 1)

    private val shapes = listOf(
        Point(originCoords),
        Line(originCoords, otherCoords1),
        Sphere(originCoords, 10),
        ArbitraryShape("any-shape", listOf(originCoords, otherCoords1, otherCoords2))
    )

    @Test
    internal fun `should have a valid point`() {
        val shape = shapes[0]

        assertThat(shape).isInstanceOf(Point::class)
        shape as Point

        assertThat(shape.name).isEqualTo("Point")
        assertThat(shape.coords).isEqualTo(originCoords)
    }

    @Test
    internal fun `should have a valid line`() {
        val shape = shapes[1]

        assertThat(shape).isInstanceOf(Line::class)
        shape as Line

        assertThat(shape.name).isEqualTo("Line")
        assertThat(shape.startCoords).isEqualTo(originCoords)
        assertThat(shape.endCoords).isEqualTo(otherCoords1)
    }

    @Test
    internal fun `should have a valid sphere`() {
        val shape = shapes[2]

        assertThat(shape).isInstanceOf(Sphere::class)
        shape as Sphere

        assertThat(shape.name).isEqualTo("Sphere")
        assertThat(shape.centerCoords).isEqualTo(originCoords)
        assertThat(shape.radius).isEqualTo(10)
    }

    @Test
    internal fun `should have a valid arbitrary shape`() {
        val shape = shapes[3]

        assertThat(shape).isInstanceOf(ArbitraryShape::class)
        shape as ArbitraryShape

        assertThat(shape.name).isEqualTo("any-shape")
        assertThat(shape.coordinates).containsExactly(originCoords, otherCoords1, otherCoords2)
    }
}
