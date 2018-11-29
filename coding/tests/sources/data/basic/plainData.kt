package tests.coding


internal val anyData = TestData.of(
	complexData,
	iterableData,
	listData,
	mapData,
	numberData,
	stringData,
	TestData.ofEncodable(
		floatData,
		booleanArrayData,
		booleanData,
		byteArrayData,
		byteData,
		doubleArrayData,
		doubleData,
		floatArrayData,
		floatData,
		intArrayData,
		intData,
		listData,
		longArrayData,
		longData,
		sequenceData,
		shortArrayData
	),
	TestData(
		nonEncodable = setOf(
			mapOf("a" to "b").entries.first()
		)
	)
)
