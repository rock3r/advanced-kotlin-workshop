package dev.sebastiano.workshop

import java.time.LocalDate

data class Supermarket(val manager: Employee, val aisles: Set<Aisle>) {

    fun findExpiredStockInAisle(aisle: Aisle): List<StockItem> = TODO()

    fun findExpiredStockByAisle(): Map<Aisle, List<StockItem>> = TODO()

    fun findMostPaidEmployee(): Employee = TODO()

    fun findEmployeeWithMostExpiredStock(): Employee? = TODO()

    fun removeExpiredItems(employee: Employee): Supermarket = TODO()

    fun sellItem(itemName: String, quantity: StockItemQuantity = 1): Supermarket = TODO()

    fun returnItem(itemName: String, quantity: StockItemQuantity = 1): Supermarket = TODO()

    fun forEachStockItem(action: (StockItem, StockItemQuantity) -> Unit) {
        TODO()
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
