package tests.coding


internal val doubleData: TestData<Double> = TestData(
	symmetric = mapOf(
		-1E200 to "-1.0E200",
		-100.999 to "-100.999",
		-100.001 to "-100.001",
		-1E-200 to "-1.0E-200",
		-0.0 to "-0.0",
		0.0 to "0.0",
		1E-200 to "1.0E-200",
		100.001 to "100.001",
		100.999 to "100.999",
		1E200 to "1.0E200"
	),
	decodableOnly = mapOf(
		"-1e20000" to Double.NEGATIVE_INFINITY,
		"-1000000000000000000000000000000" to -1000000000000000000000000000000.0,
		"-9223372036854775809" to -9223372036854775809.0, // just too large for Long
		"-1.0e+2" to -100.0,
		"-1.0e2" to -100.0,
		"-1e+2" to -100.0,
		"-1e2" to -100.0,
		"-100" to -100.0,
		"-1.9" to -1.9,
		"-1.1" to -1.1,
		"-1E0" to -1.0,
		"-1.0e-2" to -0.01,
		"-1e-2" to -0.01,
		"-1e-20000" to -0.0,
		"-0.000" to -0.0,
		"-0" to -0.0,
		"0" to 0.0,
		"0.000" to 0.0,
		"1e-20000" to 0.0,
		"1e-2" to 0.01,
		"1.0e-2" to 0.01,
		"1E0" to 1.0,
		"1.1" to 1.1,
		"1.9" to 1.9,
		"100" to 100.0,
		"1e2" to 100.0,
		"1e+2" to 100.0,
		"1.0e2" to 100.0,
		"1.0e+2" to 100.0,
		"9223372036854775808" to 9223372036854775808.0, // just too large for Long
		"1000000000000000000000000000000" to 1000000000000000000000000000000.0,
		"1e20000" to Double.POSITIVE_INFINITY
	),
	nonEncodable = setOf(
		Double.NaN,
		Double.NEGATIVE_INFINITY,
		Double.POSITIVE_INFINITY
	)
)
