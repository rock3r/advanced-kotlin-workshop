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
        val point = shape as Point

        assertThat(point.name).isEqualTo("Point")
        assertThat(point.coords).isEqualTo(originCoords)
    }

    @Test
    internal fun `should have a valid line`() {
        val shape = shapes[1]

        assertThat(shape).isInstanceOf(Line::class)
        val line = shape as Line

        assertThat(line.name).isEqualTo("Line")
        assertThat(line.startCoords).isEqualTo(originCoords)
        assertThat(line.endCoords).isEqualTo(otherCoords1)
    }

    @Test
    internal fun `should have a valid sphere`() {
        val shape = shapes[2]

        assertThat(shape).isInstanceOf(Sphere::class)
        val sphere = shape as Sphere

        assertThat(sphere.name).isEqualTo("Sphere")
        assertThat(sphere.centerCoords).isEqualTo(originCoords)
        assertThat(sphere.radius).isEqualTo(10)
    }

    @Test
    internal fun `should have a valid arbitrary shape`() {
        val shape = shapes[3]

        assertThat(shape).isInstanceOf(ArbitraryShape::class)
        val arbitraryShape = shape as ArbitraryShape

        assertThat(arbitraryShape.name).isEqualTo("any-shape")
        assertThat(arbitraryShape.coordinates).containsExactly(originCoords, otherCoords1, otherCoords2)
    }
}
