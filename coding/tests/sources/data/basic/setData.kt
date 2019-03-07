package tests.coding


internal val setData = TestData<Set<*>>(
	symmetric = mapOf(
		emptySet<Any?>() to "[]",
		setOf(1) to "[1]",
		setOf(true, "hey", null) to """[true,"hey",null]""",
		setOf(emptyList<Any?>(), listOf(1)) to "[[],[1]]"
	),
	decodableOnly = mapOf(
		" [ \t\n\r] " to emptySet<Any?>(),
		"[ [], [ 1 ] ]" to setOf(emptyList<Any?>(), listOf(1))
	)
)
