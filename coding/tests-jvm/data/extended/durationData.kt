package tests.coding

import java.time.*


internal val durationData: TestData<Duration> = TestData(
	symmetric = mapOf(
		Duration.parse("PT51H4M") to "\"PT51H4M\""
	),
	decodableOnly = mapOf(
		"\"P2DT3H4M\"" to Duration.parse("P2DT3H4M")
	)
)
