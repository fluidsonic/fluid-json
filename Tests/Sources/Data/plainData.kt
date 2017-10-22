package tests


internal val anyData = TestData.of(
	complexData,
	iterableData,
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
