package tests.coding


internal val byteArrayData: TestData<ByteArray> = TestData(
	encodableOnly = mapOf(
		byteArrayOf() to "[]",
		byteArrayOf(0) to "[0]",
		byteArrayOf(0, 1) to "[0,1]"
	)
)
