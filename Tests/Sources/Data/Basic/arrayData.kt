package tests


internal val arrayData: TestData<Array<*>> = TestData(
	encodableOnly = mapOf(
		emptyArray<String>() to "[]",
		arrayOf("one") to """["one"]""",
		arrayOf("one", "two") to """["one","two"]"""
	)
)
