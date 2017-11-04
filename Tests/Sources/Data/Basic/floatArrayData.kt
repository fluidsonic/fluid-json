package tests


internal val floatArrayData: TestData<FloatArray> = TestData(
	encodableOnly = mapOf(
		floatArrayOf() to "[]",
		floatArrayOf(0.0f) to "[0.0]",
		floatArrayOf(0.0f, 1.0f) to "[0.0,1.0]"
	),
	nonEncodable = setOf(
		floatArrayOf(Float.NaN),
		floatArrayOf(Float.NEGATIVE_INFINITY),
		floatArrayOf(Float.POSITIVE_INFINITY)
	)
)
