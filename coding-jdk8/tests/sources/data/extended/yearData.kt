package tests.coding

import java.time.Year


internal val yearData: TestData<Year> = TestData(
	symmetric = mapOf(
		Year.of(2007) to "2007"
	)
)
