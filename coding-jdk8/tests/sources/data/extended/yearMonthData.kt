package tests.coding

import java.time.YearMonth


internal val yearMonthData: TestData<YearMonth> = TestData(
	symmetric = mapOf(
		YearMonth.parse("2007-12") to "\"2007-12\""
	)
)
