package tests

import java.time.LocalDate


internal val localDateData: TestData<LocalDate> = TestData(
	symmetric = mapOf(
		LocalDate.parse("2007-12-03") to "\"2007-12-03\""
	)
)
