package tests.coding

import java.time.*


internal val monthDayData: TestData<MonthDay> = TestData(
	symmetric = mapOf(
		MonthDay.parse("--12-03") to "\"--12-03\""
	)
)
