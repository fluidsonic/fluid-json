package tests.coding

import java.time.ZoneId


internal val zoneIdData: TestData<ZoneId> = TestData(
	symmetric = mapOf(
		ZoneId.of("Europe/Berlin") to "\"Europe/Berlin\""
	)
)
