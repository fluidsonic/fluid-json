package tests


internal val plainData = TestData.of(
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
	)
)
