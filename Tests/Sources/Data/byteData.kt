package tests


internal val byteData = TestData(
	symmetric = mapOf(
		Byte.MIN_VALUE to "-128",
		(-1).toByte() to "-1",
		0.toByte() to "0",
		1.toByte() to "1",
		Byte.MAX_VALUE to "127"
	),
	decodableOnly = mapOf(
		"-1e20000" to Byte.MIN_VALUE,
		"-1000000000000000000000000000000" to Byte.MIN_VALUE,
		"-1.0e+2" to (-100).toByte(),
		"-1.0e2" to (-100).toByte(),
		"-1e+2" to (-100).toByte(),
		"-1e2" to (-100).toByte(),
		"-100" to (-100).toByte(),
		"-1.9" to (-1).toByte(),
		"-1.1" to (-1).toByte(),
		"-1E0" to (-1).toByte(),
		"-1.0e-2" to 0.toByte(),
		"-1e-2" to 0.toByte(),
		"-1e-20000" to 0.toByte(),
		"-0.000" to 0.toByte(),
		"-0" to 0.toByte(),
		"0" to 0.toByte(),
		"0.000" to 0.toByte(),
		"1e-20000" to 0.toByte(),
		"1e-2" to 0.toByte(),
		"1.0e-2" to 0.toByte(),
		"1E0" to 1.toByte(),
		"1.1" to 1.toByte(),
		"1.9" to 1.toByte(),
		"100" to 100.toByte(),
		"1e2" to 100.toByte(),
		"1e+2" to 100.toByte(),
		"1.0e2" to 100.toByte(),
		"1.0e+2" to 100.toByte(),
		"1000000000000000000000000000000" to Byte.MAX_VALUE,
		"1e20000" to Byte.MAX_VALUE
	)
)
