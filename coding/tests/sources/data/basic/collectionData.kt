package tests.coding


internal val collectionData: TestData<Collection<*>> = TestData(
	encodableOnly = mapOf(
		emptyList<String>() to "[]",
		listOf("one") to """["one"]""",
		listOf("one", "two") to """["one","two"]"""
	)
)
