package tests.coding

import java.time.*


internal val zoneIdData: TestData<ZoneId> = TestData(
	symmetric = mapOf(
		ZoneId.of("Europe/Berlin") to "\"Europe/Berlin\""
	)
)
