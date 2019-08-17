package dev.sebastiano.workshop

import assertk.Assert
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.containsOnly
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class SupermarketTest {

    private val supermarket = Supermarket(
            manager = theBigBoss,
            aisles = setOf(foodAisle, drinksAisle, clothingAisle)
    )

    @Nested
    inner class FindExpiredStockInAisle {

        @Test
        internal fun `should throw an IllegalArgumentException if the aisle is not in the supermarket`() {
            val anAisle = Aisle(4, theBigBoss, emptyMap())
            assertThat { supermarket.findExpiredStockInAisle(anAisle) }.isFailure()
                    .isInstanceOf(IllegalArgumentException::class)
        }

        @Test
        internal fun `should return an empty list if the aisle has no stock`() {
            val emptyAisle = foodAisle.copy(stock = emptyMap())
            val supermarket = Supermarket(theBigBoss, setOf(emptyAisle))
            assertThat(supermarket.findExpiredStockInAisle(emptyAisle)).isEmpty()
        }

        @Test
        internal fun `should return an empty list if the aisle has no expired stock`() {
            assertThat(supermarket.findExpiredStockInAisle(drinksAisle)).isEmpty()
        }

        @Test
        internal fun `should return an empty list if the aisle has no stock with an expiry`() {
            assertThat(supermarket.findExpiredStockInAisle(clothingAisle)).isEmpty()
        }

        @Test
        internal fun `should return a list of expired items if the aisle has some stock with an expiry in the past or today`() {
            assertThat(supermarket.findExpiredStockInAisle(foodAisle)).containsExactly(
                    StockItem("Banana", 180, today),
                    StockItem("Bread", 280, today),
                    StockItem("Chicken", 320, lastWeek)
            )
        }
    }

    @Nested
    inner class FindExpiredStockByAisle {

        @Test
        internal fun `should return an empty map if the supermarket has no aisles`() {
            val emptySupermarket = Supermarket(theBigBoss, emptySet())
            assertThat(emptySupermarket.findExpiredStockByAisle()).isEmpty()
        }

        @Test
        internal fun `should return an empty map if the aisles are empty`() {
            val emptySupermarket = Supermarket(
                    theBigBoss,
                    setOf(foodAisle.copy(stock = emptyMap()), drinksAisle.copy(stock = emptyMap()))
            )
            assertThat(emptySupermarket.findExpiredStockByAisle()).isEmpty()
        }

        @Test
        internal fun `should return an empty map if the aisles have no expired stock`() {
            val testSupermarket = supermarket.copy(aisles = setOf(drinksAisle, clothingAisle))
            assertThat(testSupermarket.findExpiredStockByAisle()).isEmpty()
        }

        @Test
        internal fun `should return a map of expired items by aisle if the aisles have some stock with an expiry in the past or today`() {
            assertThat(supermarket.findExpiredStockByAisle()).containsExactly(
                    foodAisle to listOf(
                            StockItem("Banana", 180, today),
                            StockItem("Bread", 280, today),
                            StockItem("Chicken", 320, lastWeek)
                    )
            )
        }
    }

    @Nested
    inner class FindMostPaidEmployee {

        @Test
        internal fun `should return the manager if there are no aisles`() {
            val supermarket = supermarket.copy(aisles = emptySet())
            assertThat(supermarket.findMostPaidEmployee()).isEqualTo(theBigBoss)
        }

        @Test
        internal fun `should return the manager if the employees all have lower salaries`() {
            assertThat(supermarket.findMostPaidEmployee()).isEqualTo(theBigBoss)
        }

        @Test
        internal fun `should return the mysterious stranger if other employees and manager all have lower salaries`() {
            val theMysteriousStranger = Employee("Mysterious", "Stranger", -1, "?", 100000)
            val supermarket = supermarket.copy(
                    aisles = setOf(
                            foodAisle,
                            drinksAisle,
                            clothingAisle.copy(stocker = theMysteriousStranger)
                    )
            )
            assertThat(supermarket.findMostPaidEmployee()).isEqualTo(theMysteriousStranger)
        }
    }

    @Nested
    inner class FindEmployeeWithMostExpiredStock {

        @Test
        internal fun `should return null if the supermarket has no aisles`() {
            val emptySupermarket = Supermarket(theBigBoss, emptySet())
            assertThat(emptySupermarket.findEmployeeWithMostExpiredStock()).isNull()
        }

        @Test
        internal fun `should return null if the aisles are empty`() {
            val emptySupermarket = Supermarket(
                    theBigBoss,
                    setOf(foodAisle.copy(stock = emptyMap()), drinksAisle.copy(stock = emptyMap()))
            )
            assertThat(emptySupermarket.findEmployeeWithMostExpiredStock()).isNull()
        }

        @Test
        internal fun `should return null if the aisles have no expired stock`() {
            val testSupermarket = supermarket.copy(aisles = setOf(drinksAisle, clothingAisle))
            assertThat(testSupermarket.findEmployeeWithMostExpiredStock()).isNull()
        }

        @Test
        internal fun `should return the employee with most expired stock if the aisles have some stock with an expiry in the past or today`() {
            assertThat(supermarket.findEmployeeWithMostExpiredStock()).isEqualTo(employeeOne)
        }
    }

    @Nested
    inner class RestockExpiredItems {

        @Test
        internal fun `should throw an IllegalArgumentException if the employee is not in the supermarket`() {
            val theMysteriousStranger = Employee("Mysterious", "Stranger", -1, "?", 100000)
            assertThat { supermarket.removeExpiredItems(theMysteriousStranger) }.isFailure()
                    .isInstanceOf(IllegalArgumentException::class)
        }

        @Test
        internal fun `should return an identical supermarket if the supermarket has no stock`() {
            val emptyAisle = foodAisle.copy(stock = emptyMap())
            val supermarket = Supermarket(theBigBoss, setOf(emptyAisle))
            assertThat(supermarket.removeExpiredItems(emptyAisle.stocker)).isEqualTo(supermarket)
        }

        @Test
        internal fun `should return an identical supermarket if the supermarket has no expired stock`() {
            val supermarket = Supermarket(theBigBoss, setOf(drinksAisle, clothingAisle))
            assertThat(supermarket.removeExpiredItems(employeeTwo)).isEqualTo(supermarket)
            assertThat(supermarket.removeExpiredItems(employeeThree)).isEqualTo(supermarket)
        }

        @Test
        internal fun `should return a list of expired items if the aisle has some stock with an expiry in the past or today`() {
            assertThat(supermarket.removeExpiredItems(employeeOne)).isEqualTo(
                    supermarket.copy(
                            aisles = setOf(
                                    foodAisle.copy(
                                            stock = mapOf(
                                                    StockItem("Apple", 140, tomorrow) to 100,
                                                    StockItem("Beef", 450, tomorrow) to 20,
                                                    StockItem("Salt", 320) to 60,
                                                    StockItem("Pepper", 320) to 40,
                                                    StockItem("Oil", 320) to 50
                                            )
                                    ),
                                    drinksAisle,
                                    clothingAisle
                            )
                    )
            )
        }
    }

    @Nested
    inner class SellItem {

        @Test
        internal fun `should throw an NoSuchElementException if the item is not stocked in the supermarket`() {
            assertThat { supermarket.sellItem("Potato") }.isFailure()
                    .isInstanceOf(NoSuchElementException::class)
        }

        @Test
        internal fun `should throw an IllegalArgumentException if the quantity is zero`() {
            assertThat { supermarket.sellItem("Bread", 0) }.isFailure()
                    .isInstanceOf(IllegalArgumentException::class)
        }

        @Test
        internal fun `should throw an IllegalArgumentException if the quantity is negative`() {
            assertThat { supermarket.sellItem("Bread", -1) }.isFailure()
                    .isInstanceOf(IllegalArgumentException::class)
        }

        @Test
        internal fun `should return a supermarket with reduced stock for the item if there's more of the item in stock than sold`() {
            assertThat(supermarket.sellItem("Banana", 10)).isIdenticalTo(
                    supermarket.copy(
                            aisles = setOf(
                                    foodAisle.copy(
                                            stock = mapOf(
                                                    StockItem("Banana", 180, today) to 90,
                                                    StockItem("Apple", 140, tomorrow) to 100,
                                                    StockItem("Bread", 280, today) to 80,
                                                    StockItem("Beef", 450, tomorrow) to 20,
                                                    StockItem("Chicken", 320, lastWeek) to 40,
                                                    StockItem("Salt", 320) to 60,
                                                    StockItem("Pepper", 320) to 40,
                                                    StockItem("Oil", 320) to 50
                                            )
                                    ),
                                    drinksAisle,
                                    clothingAisle
                            )
                    )
            )
        }

        @Test
        internal fun `should return a supermarket without the item in stock if there's exactly the same quantity of the item in stock than sold`() {
            assertThat(supermarket.sellItem("Banana", 100)).isIdenticalTo(
                    supermarket.copy(
                            aisles = setOf(
                                    foodAisle.copy(
                                            stock = mapOf(
                                                    StockItem("Apple", 140, tomorrow) to 100,
                                                    StockItem("Bread", 280, today) to 80,
                                                    StockItem("Beef", 450, tomorrow) to 20,
                                                    StockItem("Chicken", 320, lastWeek) to 40,
                                                    StockItem("Salt", 320) to 60,
                                                    StockItem("Pepper", 320) to 40,
                                                    StockItem("Oil", 320) to 50
                                            )
                                    ),
                                    drinksAisle,
                                    clothingAisle
                            )
                    )
            )
        }

        @Test
        internal fun `should throw IllegalArgumentException if there's less of the item in stock than sold`() {
            assertThat { supermarket.sellItem("Banana", 101) }.isFailure()
                    .isInstanceOf(IllegalArgumentException::class)
        }
    }

    @Nested
    inner class ReturnItem {

        @Test
        internal fun `should throw IllegalArgumentException if the quantity is zero`() {
            assertThat { supermarket.returnItem("Potato", 0) }.isFailure()
                    .isInstanceOf(IllegalArgumentException::class)
        }

        @Test
        internal fun `should throw IllegalArgumentException if the quantity is negative`() {
            assertThat { supermarket.returnItem("Potato", -1) }.isFailure()
                    .isInstanceOf(IllegalArgumentException::class)
        }

        @Test
        internal fun `should throw NoSuchElementException if the item is not already in stock`() {
            assertThat { supermarket.returnItem("Potato") }.isFailure()
                    .isInstanceOf(NoSuchElementException::class)
        }

        @Test
        internal fun `should increment the stock quantity for the item if it's already in stock`() {
            assertThat(supermarket.returnItem("Bread", 10)).isIdenticalTo(
                    supermarket.copy(
                            aisles = setOf(
                                    foodAisle.copy(
                                            stock = mapOf(
                                                    StockItem("Banana", 180, today) to 100,
                                                    StockItem("Apple", 140, tomorrow) to 100,
                                                    StockItem("Bread", 280, today) to 90,
                                                    StockItem("Beef", 450, tomorrow) to 20,
                                                    StockItem("Chicken", 320, lastWeek) to 40,
                                                    StockItem("Salt", 320) to 60,
                                                    StockItem("Pepper", 320) to 40,
                                                    StockItem("Oil", 320) to 50
                                            )
                                    ),
                                    drinksAisle,
                                    clothingAisle
                            )
                    )
            )
        }
    }
}

private fun Assert<Map<Aisle, List<StockItem>>>.containsExactly(vararg itemsByAisle: Pair<Aisle, List<StockItem>>) {
    given { actual ->
        assertThat(actual.size).isEqualTo(itemsByAisle.size)
        for ((aisle, expectedItems) in itemsByAisle) {
            assertThat(actual[aisle]).isNotNull()
            assertThat(actual.getValue(aisle)).containsExactly(*expectedItems.toTypedArray())
        }
    }
}

private fun Assert<Supermarket>.isIdenticalTo(expected: Supermarket) {
    given { actual ->
        assertThat(actual.manager).isEqualTo(expected.manager)
        assertThat(actual.aisles.size).isEqualTo(expected.aisles.size)

        fun Aisle.isEquivalentTo(other: Aisle) =
                number == other.number &&
                        stocker == other.stocker &&
                        stock.size == other.stock.size

        for (aisle in actual.aisles) {
            val correspondingAisle = expected.aisles.firstOrNull { it.isEquivalentTo(aisle) }
            assertThat(correspondingAisle).isNotNull()
            val expectedPairs = aisle.stock.entries.map { it.toPair() }.toTypedArray()
            assertThat(correspondingAisle!!.stock).containsOnly(*expectedPairs)
        }
    }
}

private val today = LocalDate.now()
private val tomorrow = today.plusDays(1)
private val nextWeek = today.plusDays(7)
private val lastWeek = today.minusDays(7)

private val theBigBoss = Employee("Ton", "Gori", 47, "Store Manager", 45000)
private val employeeOne = Employee("Retsuko", "Gori", 26, "Stock Clerk", 24500)
private val employeeTwo = Employee("Haida", "Tsunoda", 32, "Stock Clerk", 27300)
private val employeeThree = Employee("Tsubone", "Kabae", 38, "Senior Stock Clerk", 31000)

private val foodAisle = Aisle(
        number = 1,
        stocker = employeeOne,
        stock = mapOf(
                StockItem("Banana", 180, today) to 100,
                StockItem("Apple", 140, tomorrow) to 100,
                StockItem("Bread", 280, today) to 80,
                StockItem("Beef", 450, tomorrow) to 20,
                StockItem("Chicken", 320, lastWeek) to 40,
                StockItem("Salt", 320) to 60,
                StockItem("Pepper", 320) to 40,
                StockItem("Oil", 320) to 50
        )
)

private val drinksAisle = Aisle(
        number = 2,
        stocker = employeeTwo,
        stock = mapOf(
                StockItem("Still water", 25, nextWeek) to 90,
                StockItem("Sparkling water", 25, nextWeek) to 90,
                StockItem("Lemonade", 160, tomorrow) to 20,
                StockItem("Cola", 160, tomorrow) to 35,
                StockItem("Orange juice", 220, tomorrow) to 20
        )
)

private val clothingAisle = Aisle(
        number = 3,
        stocker = employeeThree,
        stock = mapOf(
                StockItem("Shoes", 6000) to 45,
                StockItem("T-shirt", 1699) to 75,
                StockItem("Trousers", 2420) to 30,
                StockItem("Pants", 895) to 40,
                StockItem("Socks", 345) to 50
        )
)
