package tests.coding


internal val charData: TestData<Char> = TestData(
	symmetric = mapOf(
		'a' to """"a"""",
		0.toChar() to """"\u0000"""",
		65535.toChar() to "\"\uFFFF\""
	)
)
