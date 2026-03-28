package tests.coding

import java.time.*


internal val localDateTimeData: TestData<LocalDateTime> = TestData(
	symmetric = mapOf(
		LocalDateTime.parse("2007-12-03T10:15:30") to "\"2007-12-03T10:15:30\""
	)
)
