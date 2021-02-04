package tests.coding

import java.time.*


internal val localTimeData: TestData<LocalTime> = TestData(
	symmetric = mapOf(
		LocalTime.parse("10:15:30") to "\"10:15:30\""
	)
)
