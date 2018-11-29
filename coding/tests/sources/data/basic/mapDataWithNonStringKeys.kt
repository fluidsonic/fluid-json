package tests.coding


internal val mapDataWithNonStringKeys = TestData<Map<YearMonthDay, *>>(
	symmetric = mapOf(
		emptyMap<YearMonthDay, Any?>() to "{}",
		mapOf(YearMonthDay(2017, 10, 29) to 1) to """{"2017-10-29":1}""",
		mapOf(
			YearMonthDay(2017, 10, 27) to true,
			YearMonthDay(2017, 10, 28) to "hey",
			YearMonthDay(2017, 10, 29) to null
		) to """{"2017-10-27":true,"2017-10-28":"hey","2017-10-29":null}""",
		mapOf(
			YearMonthDay(2017, 10, 27) to emptyMap<YearMonthDay, Any?>(),
			YearMonthDay(2017, 10, 28) to mapOf("2017-10-29" to 1)
		) to """{"2017-10-27":{},"2017-10-28":{"2017-10-29":1}}"""
	)
)
