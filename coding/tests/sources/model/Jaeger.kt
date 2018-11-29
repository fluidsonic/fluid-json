package tests.coding


internal data class Jaeger(
	val height: Double,
	val launchDate: YearMonthDay,
	val mark: Int,
	val name: String,
	val origin: String,
	val status: Status,
	val weight: Double
) {

	enum class Status {

		destroyed
	}
}
