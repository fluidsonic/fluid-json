package tests

import java.time.DayOfWeek


internal val dayOfWeekData: TestData<DayOfWeek> = TestData(
	symmetric = mapOf(
		DayOfWeek.MONDAY to "\"monday\"",
		DayOfWeek.TUESDAY to "\"tuesday\"",
		DayOfWeek.WEDNESDAY to "\"wednesday\"",
		DayOfWeek.THURSDAY to "\"thursday\"",
		DayOfWeek.FRIDAY to "\"friday\"",
		DayOfWeek.SATURDAY to "\"saturday\"",
		DayOfWeek.SUNDAY to "\"sunday\""
	)
)
