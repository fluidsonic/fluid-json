package tests.coding

import java.time.*


internal val offsetTimeData: TestData<OffsetTime> = TestData(
	symmetric = mapOf(
		OffsetTime.parse("10:15:30+01:00") to "\"10:15:30+01:00\""
	)
)
