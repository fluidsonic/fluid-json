package tests.coding

import java.time.*


internal val zoneOffsetData: TestData<ZoneOffset> = TestData(
	symmetric = mapOf(
		ZoneOffset.of("+01:00") to "\"+01:00\""
	)
)
