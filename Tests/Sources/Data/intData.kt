package tests


internal val intData = TestData(
	symmetric = mapOf(
		Int.MIN_VALUE to "-2147483648",
		-1 to "-1",
		0 to "0",
		1 to "1",
		Int.MAX_VALUE to "2147483647"
	),
	decodableOnly = mapOf(
		"-1e20000" to Int.MIN_VALUE,
		"-1000000000000000000000000000000" to Int.MIN_VALUE,
		"-1.0e+2" to -100,
		"-1.0e2" to -100,
		"-1e+2" to -100,
		"-1e2" to -100,
		"-100" to -100,
		"-1.9" to -1,
		"-1.1" to -1,
		"-1E0" to -1,
		"-1.0e-2" to 0,
		"-1e-2" to 0,
		"-1e-20000" to 0,
		"-0.000" to 0,
		"-0" to 0,
		"0" to 0,
		"0.000" to 0,
		"1e-20000" to 0,
		"1e-2" to 0,
		"1.0e-2" to 0,
		"1E0" to 1,
		"1.1" to 1,
		"1.9" to 1,
		"100" to 100,
		"1e2" to 100,
		"1e+2" to 100,
		"1.0e2" to 100,
		"1.0e+2" to 100,
		"1000000000000000000000000000000" to Int.MAX_VALUE,
		"1e20000" to Int.MAX_VALUE
	)
)
