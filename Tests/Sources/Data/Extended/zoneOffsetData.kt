package tests

import java.time.ZoneOffset


internal val zoneOffsetData: TestData<ZoneOffset> = TestData(
	symmetric = mapOf(
		ZoneOffset.of("+01:00") to "\"+01:00\""
	)
)
