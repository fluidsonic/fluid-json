package tests


internal val booleanArrayData = TestData(
	encodableOnly = mapOf(
		booleanArrayOf() to "[]",
		booleanArrayOf(true) to "[true]",
		booleanArrayOf(true, false) to "[true,false]"
	)
)
