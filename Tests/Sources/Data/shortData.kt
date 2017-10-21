package tests


internal val shortData: TestData<Short> = TestData(
	symmetric = mapOf(
		Short.MIN_VALUE to "-32768",
		(-1).toShort() to "-1",
		0.toShort() to "0",
		1.toShort() to "1",
		Short.MAX_VALUE to "32767"
	),
	decodableOnly = mapOf(
		"-1e20000" to Short.MIN_VALUE,
		"-1000000000000000000000000000000" to Short.MIN_VALUE,
		"-1.0e+2" to (-100).toShort(),
		"-1.0e2" to (-100).toShort(),
		"-1e+2" to (-100).toShort(),
		"-1e2" to (-100).toShort(),
		"-100" to (-100).toShort(),
		"-1000000000000000000000000000000e-30" to (-1).toShort(),
		"-1.9" to (-1).toShort(),
		"-1.1" to (-1).toShort(),
		"-1E0" to (-1).toShort(),
		"-1.0e-2" to 0.toShort(),
		"-1e-2" to 0.toShort(),
		"-1e-20000" to 0.toShort(),
		"-0.000" to 0.toShort(),
		"-0" to 0.toShort(),
		"0" to 0.toShort(),
		"0.000" to 0.toShort(),
		"1e-20000" to 0.toShort(),
		"1e-2" to 0.toShort(),
		"1.0e-2" to 0.toShort(),
		"1E0" to 1.toShort(),
		"1.1" to 1.toShort(),
		"1.9" to 1.toShort(),
		"1000000000000000000000000000000e-30" to 1.toShort(),
		"100" to 100.toShort(),
		"1e2" to 100.toShort(),
		"1e+2" to 100.toShort(),
		"1.0e2" to 100.toShort(),
		"1.0e+2" to 100.toShort(),
		"1000000000000000000000000000000" to Short.MAX_VALUE,
		"1e20000" to Short.MAX_VALUE
	)
)
