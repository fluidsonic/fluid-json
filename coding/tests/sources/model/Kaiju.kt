package tests.coding


internal data class Kaiju(
	val breachDate: YearMonthDay,
	val category: Int,
	val height: Double,
	val name: String,
	val origin: String,
	val status: Status,
	val weight: Double
) {

	enum class Status {

		deceased
	}
}
