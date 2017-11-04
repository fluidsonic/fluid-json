package tests

import java.time.Period


internal val periodData: TestData<Period> = TestData(
	symmetric = mapOf(
		Period.parse("P1Y2M25D") to "\"P1Y2M25D\""
	),
	decodableOnly = mapOf(
		"\"P1Y2M3W4D\"" to Period.parse("P1Y2M3W4D")
	)
)
