package tests.coding


internal val floatData: TestData<Float> = TestData(
	symmetric = mapOf(
		-1E38f to "-1.0E38",
		-100.999f to "-100.999",
		-100.001f to "-100.001",
		-1E-38f to "-1.0E-38",
		-0.0f to "-0.0",
		0.0f to "0.0",
		1E-38f to "1.0E-38",
		100.001f to "100.001",
		100.999f to "100.999",
		1E38f to "1.0E38"
	),
	decodableOnly = mapOf(
		"-1e20000" to Float.NEGATIVE_INFINITY,
		"-1000000000000000000000000000000" to -1000000000000000000000000000000.0f,
		"-9223372036854775809" to -9223372036854775809.0f, // just too large for Long
		"-1.0e+2" to -100.0f,
		"-1.0e2" to -100.0f,
		"-1e+2" to -100.0f,
		"-1e2" to -100.0f,
		"-100" to -100.0f,
		"-1E0" to -1.0f,
		"-1.0e-2" to -0.01f,
		"-1e-2" to -0.01f,
		"-1e-20000" to -0.0f,
		"-0.000" to -0.0f,
		"-0" to -0.0f,
		"0" to 0.0f,
		"0.000" to 0.0f,
		"1e-20000" to 0.0f,
		"1e-2" to 0.01f,
		"1.0e-2" to 0.01f,
		"1E0" to 1.0f,
		"100" to 100.0f,
		"1e2" to 100.0f,
		"1e+2" to 100.0f,
		"1.0e2" to 100.0f,
		"1.0e+2" to 100.0f,
		"9223372036854775808" to 9223372036854775808.0f, // just too large for Long
		"1000000000000000000000000000000" to 1000000000000000000000000000000.0f,
		"1e20000" to Float.POSITIVE_INFINITY
	),
	nonEncodable = setOf(
		Float.NaN,
		Float.NEGATIVE_INFINITY,
		Float.POSITIVE_INFINITY
	)
)
