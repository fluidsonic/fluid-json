package tests.coding


internal val charArrayData: TestData<CharArray> = TestData(
	encodableOnly = mapOf(
		charArrayOf() to "[]",
		charArrayOf(0.toChar()) to """["\u0000"]""",
		charArrayOf(0.toChar(), 'a') to """["\u0000","a"]"""
	)
)
