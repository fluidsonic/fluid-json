package tests


internal val longData = TestData(
	symmetric = mapOf(
		Long.MIN_VALUE to "-9223372036854775808",
		-1L to "-1",
		0L to "0",
		1L to "1",
		Long.MAX_VALUE to "9223372036854775807"
	),
	decodableOnly = mapOf(
		"-1e20000" to Long.MIN_VALUE,
		"-1000000000000000000000000000000" to Long.MIN_VALUE,
		"-1.0e+2" to -100L,
		"-1.0e2" to -100L,
		"-1e+2" to -100L,
		"-1e2" to -100L,
		"-100" to -100L,
		"-1.9" to -1L,
		"-1.1" to -1L,
		"-1E0" to -1L,
		"-1.0e-2" to 0L,
		"-1e-2" to 0L,
		"-1e-20000" to 0L,
		"-0.000" to 0L,
		"-0" to 0L,
		"0" to 0L,
		"0.000" to 0L,
		"1e-20000" to 0L,
		"1e-2" to 0L,
		"1.0e-2" to 0L,
		"1E0" to 1L,
		"1.1" to 1L,
		"1.9" to 1L,
		"100" to 100L,
		"1e2" to 100L,
		"1e+2" to 100L,
		"1.0e2" to 100L,
		"1.0e+2" to 100L,
		"1000000000000000000000000000000" to Long.MAX_VALUE,
		"1e20000" to Long.MAX_VALUE
	)
)
