package tests.coding


internal val booleanArrayData: TestData<BooleanArray> = TestData(
	encodableOnly = mapOf(
		booleanArrayOf() to "[]",
		booleanArrayOf(true) to "[true]",
		booleanArrayOf(true, false) to "[true,false]"
	)
)
