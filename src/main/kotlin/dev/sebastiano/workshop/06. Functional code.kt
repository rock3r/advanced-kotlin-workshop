package dev.sebastiano.workshop

import java.time.LocalDate

data class Supermarket(val manager: Employee, val aisles: Set<Aisle>) {

    init {
        val aisleNumbers = aisles.map { it.number }
        require(aisleNumbers.sorted() == aisleNumbers) { " The aisles must be sorted by number" }
    }

    fun findExpiredStockInAisle(aisle: Aisle): List<StockItem> {
        require(aisles.contains(aisle)) { "The aisle doesn't exist" }

        val now = LocalDate.now()
        return aisles.first { it == aisle }
            .stock
            .map { it.key }
            .filter { it.isExpired(now) }
            .toList()
    }

    private fun StockItem.isExpired(atWhatDate: LocalDate) = expiryDate != null && expiryDate <= atWhatDate

    fun findExpiredStockByAisle(): Map<Aisle, List<StockItem>> =
        aisles.flatMap { aisle -> findExpiredStockInAisle(aisle).map { aisle to it } }
            .groupBy { it.first }
            .mapValues { entry ->
                entry.value.map { aisleToItem -> aisleToItem.second }
            }

    fun findMostPaidEmployee(): Employee = aisles.map { it.stocker }
        .plusElement(manager)
        .maxByOrNull { it.salary }!!

    fun findEmployeeWithMostExpiredStock(): Employee? = findExpiredStockByAisle()
        .toList()
        .sortedByDescending { it.second.count() }
        .map { it.first.stocker }
        .firstOrNull()

    fun removeExpiredItems(employee: Employee): Supermarket {
        val aisle = aisles.firstOrNull { it.stocker == employee }
        require(aisle != null) { "The employee doesn't work here" }

        val now = LocalDate.now()
        return copy(
            aisles = aisles.filter { it != aisle }
                .plus(
                    aisle.copy(
                        stock = aisle.stock.filter { it.key.isNotExpired(now) }
                    )
                )
                .sortedBy { it.number }
                .toSet()
        )
    }

    private fun StockItem.isNotExpired(atWhatDate: LocalDate) = expiryDate == null || expiryDate > atWhatDate

    fun sellItem(itemName: String, quantity: StockItemQuantity = 1): Supermarket {
        require(quantity > 0) { "You need to sell at least one item" }

        val (aisle, stockItemWithQty) = findStockItemByName(itemName)

        val (stockItem, _) = stockItemWithQty
        return mapStockQuantity(aisle) { currentItem, stockQuantity ->
            if (currentItem == stockItem) {
                require(stockQuantity >= quantity) { "Not enough of '${stockItem.name} in stock to sell" }
                stockQuantity - quantity
            } else stockQuantity
        }
    }

    fun returnItem(itemName: String, quantity: StockItemQuantity = 1): Supermarket {
        require(quantity > 0) { "You need to return at least one item" }

        val (aisle, stockItemWithQty) = findStockItemByName(itemName)

        val (stockItem, _) = stockItemWithQty
        return mapStockQuantity(aisle) { currentItem, stockQuantity ->
            if (currentItem == stockItem) {
                stockQuantity + quantity
            } else stockQuantity
        }
    }

    private fun findStockItemByName(itemName: String) =
        aisles.flatMap { aisle -> aisle.stock.map { stockItemWithQty -> aisle to stockItemWithQty } }
            .first { (_, stockItemWithQty) -> stockItemWithQty.key.name.equals(itemName, ignoreCase = true) }

    private fun mapStockQuantity(aisle: Aisle, mapper: (StockItem, StockItemQuantity) -> Int): Supermarket = copy(
        aisles = aisles.filter { it != aisle }
            .plus(
                aisle.copy(
                    stock = aisle.stock.mapValues { mapper(it.key, it.value) }
                        .filterValues { stockQuantity ->
                            stockQuantity > 0
                        }
                )
            )
            .sortedBy { it.number }
            .toSet()
    )

    fun forEachStockItem(action: (StockItem, StockItemQuantity) -> Unit) {
        aisles.flatMap { aisle -> aisle.stock.entries }
            .forEach { (stockItem, quantity) -> action(stockItem, quantity) }
    }
}

data class Aisle(
    val number: Int,
    val stocker: Employee,
    val stock: Map<StockItem, StockItemQuantity>
)

data class Employee(
    val firstName: String,
    val lastName: String,
    val age: Int,
    val role: String,
    val salary: Int
)

data class StockItem(
    val name: String,
    val priceCents: Int,
    val expiryDate: LocalDate? = null
)

typealias StockItemQuantity = Int
