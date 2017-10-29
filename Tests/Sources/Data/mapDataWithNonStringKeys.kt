package tests

import java.time.LocalDate


internal val mapDataWithNonStringKeys = TestData<Map<LocalDate, *>>(
	symmetric = mapOf(
		emptyMap<LocalDate, Any?>() to "{}",
		mapOf(LocalDate.of(2017, 10, 29) to 1) to """{"2017-10-29":1}""",
		mapOf(
			LocalDate.of(2017, 10, 27) to true,
			LocalDate.of(2017, 10, 28) to "hey",
			LocalDate.of(2017, 10, 29) to null
		) to """{"2017-10-27":true,"2017-10-28":"hey","2017-10-29":null}""",
		mapOf(
			LocalDate.of(2017, 10, 27) to emptyMap<LocalDate, Any?>(),
			LocalDate.of(2017, 10, 28) to mapOf("2017-10-29" to 1)
		) to """{"2017-10-27":{},"2017-10-28":{"2017-10-29":1}}"""
	)
)
