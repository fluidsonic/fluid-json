package tests

import java.time.OffsetDateTime


internal val offsetDateTimeData: TestData<OffsetDateTime> = TestData(
	symmetric = mapOf(
		OffsetDateTime.parse("2007-12-03T10:15:30+01:00") to "\"2007-12-03T10:15:30+01:00\""
	)
)
