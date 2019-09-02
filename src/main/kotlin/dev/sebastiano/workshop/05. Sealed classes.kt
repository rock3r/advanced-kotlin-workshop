package dev.sebastiano.workshop

sealed class ThreeDimensionalShape(open val name: String) {

    data class Point(val coords: Coords) : ThreeDimensionalShape("Point")

    data class Line(val startCoords: Coords, val endCoords: Coords) : ThreeDimensionalShape("Line")

    data class Sphere(val centerCoords: Coords, val radius: Int) : ThreeDimensionalShape("Sphere")

    data class ArbitraryShape(override val name: String, val coordinates: List<Coords>) : ThreeDimensionalShape(name)
}

data class Coords(val x: Int, val y: Int, val z: Int)
