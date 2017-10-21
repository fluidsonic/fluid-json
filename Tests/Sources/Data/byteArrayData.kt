package tests


internal val byteArrayData = TestData(
	encodableOnly = mapOf(
		byteArrayOf() to "[]",
		byteArrayOf(0) to "[0]",
		byteArrayOf(0, 1) to "[0,1]"
	)
)
