package tests


internal val doubleArrayData: TestData<DoubleArray> = TestData(
	encodableOnly = mapOf(
		doubleArrayOf() to "[]",
		doubleArrayOf(0.0) to "[0.0]",
		doubleArrayOf(0.0, 1.0) to "[0.0,1.0]"
	)
)
