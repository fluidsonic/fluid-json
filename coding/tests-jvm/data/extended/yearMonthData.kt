package tests.coding

import java.time.*


internal val yearMonthData: TestData<YearMonth> = TestData(
	symmetric = mapOf(
		YearMonth.parse("2007-12") to "\"2007-12\""
	)
)
