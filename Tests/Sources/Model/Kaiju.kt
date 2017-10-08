package tests

import java.time.LocalDate


internal data class Kaiju(
	val breachDate: LocalDate,
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
