package tests

import java.time.LocalDate

// data from http://pacificrim.wikia.com

internal object TestData {

	val jaegers = listOf(
		Jaeger(
			height = 76.2,
			lauchDate = LocalDate.of(2019, 11, 2),
			mark = 5,
			name = "Striker Eureka",
			origin = "Australia",
			status = Jaeger.Status.destroyed,
			weight = 1_850.0
		)
	)


	val kaijus = listOf(
		Kaiju(
			breachDate = LocalDate.of(2025, 1, 12),
			category = 5,
			height = 181.7,
			name = "Slattern",
			origin = "Anteverse",
			status = Kaiju.Status.deceased,
			weight = 6_750.0
		)
	)
}
