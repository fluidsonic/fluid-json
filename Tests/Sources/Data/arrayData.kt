package tests


internal val arrayData = TestData(
	encodableOnly = mapOf(
		emptyArray<String>() to "[]",
		arrayOf("one") to """["one"]""",
		arrayOf("one", "two") to """["one","two"]"""
	)
)
