package tests.coding


internal val collectionData: TestData<Collection<*>> = TestData(
	symmetric = mapOf(
		emptyList<String>() to "[]",
		listOf("one") to """["one"]""",
		listOf("one", "two") to """["one","two"]"""
	)
)
