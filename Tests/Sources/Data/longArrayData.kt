package tests


internal val longArrayData: TestData<LongArray> = TestData(
	encodableOnly = mapOf(
		longArrayOf() to "[]",
		longArrayOf(0) to "[0]",
		longArrayOf(0, 1) to "[0,1]"
	)
)
