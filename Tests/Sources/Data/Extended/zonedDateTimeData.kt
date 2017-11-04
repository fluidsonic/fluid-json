package tests

import java.time.ZonedDateTime


internal val zonedDateTimeData: TestData<ZonedDateTime> = TestData(
	symmetric = mapOf(
		ZonedDateTime.parse("2007-12-03T10:15:30+01:00[Europe/Paris]") to "\"2007-12-03T10:15:30+01:00[Europe/Paris]\""
	)
)
