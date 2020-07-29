package tests.coding


@Suppress("RedundantAsSequence")
internal val iterableData = TestData<Iterable<*>>(
	symmetric = mapOf(
		emptyList<Any?>() to "[]",
		listOf(1) to "[1]",
		listOf(true, "hey", null) to """[true,"hey",null]""",
		listOf(emptyList<Any?>(), listOf(1)) to "[[],[1]]"
	),
	decodableOnly = mapOf(
		" [ \t\n\r] " to emptyList<Any?>(),
		"[ [], [ 1 ] ]" to listOf(emptyList<Any?>(), listOf(1))
	),
	encodableOnly = mapOf(
		emptyList<Any?>().asSequence().asIterable() to "[]",
		listOf(1).asSequence().asIterable() to "[1]",
		listOf(true, "hey", null).asSequence().asIterable() to """[true,"hey",null]""",
		listOf(emptyList<Any?>(), listOf(1)).asSequence().asIterable() to "[[],[1]]"
	)
)
