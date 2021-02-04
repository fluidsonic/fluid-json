package tests.coding


internal val sequenceData = TestData<Sequence<*>>(
	symmetric = mapOf(
		emptyEquatableSequence<Any?>() to "[]",
		equatableSequenceOf(1) to "[1]",
		equatableSequenceOf(true, "hey", null) to """[true,"hey",null]""",
		equatableSequenceOf(emptyList<Any?>(), listOf(1)) to "[[],[1]]"
	),
	decodableOnly = mapOf(
		" [ \t\n\r] " to emptyEquatableSequence<Any?>(),
		"[ [], [ 1 ] ]" to equatableSequenceOf(emptyList<Any?>(), listOf(1))
	)
)
