package tests


internal val shortArrayData = TestData(
	encodableOnly = mapOf(
		shortArrayOf() to "[]",
		shortArrayOf(0) to "[0]",
		shortArrayOf(0, 1) to "[0,1]"
	)
)
