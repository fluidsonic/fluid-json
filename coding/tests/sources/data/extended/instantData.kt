package tests.coding

import java.time.Instant


internal val instantData: TestData<Instant> = TestData(
	symmetric = mapOf(
		Instant.parse("2007-12-03T10:15:30Z") to "\"2007-12-03T10:15:30Z\""
	),
	decodableOnly = mapOf(
		"\"2007-12-03T10:15:30.00Z\"" to Instant.parse("2007-12-03T10:15:30.00Z")
	)
)
