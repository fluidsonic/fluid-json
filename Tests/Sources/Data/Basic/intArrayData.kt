package tests


internal val intArrayData: TestData<IntArray> = TestData(
	encodableOnly = mapOf(
		intArrayOf() to "[]",
		intArrayOf(0) to "[0]",
		intArrayOf(0, 1) to "[0,1]"
	)
)
