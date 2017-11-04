package tests


internal val complexData: TestData<Map<String, Any?>> = TestData(
	decodableOnly = mapOf(
		"""
			{
				"true":    true,
				"false":   false,
				"null":    null,
				"1":       1,
				"-1.0e-2": -1.0e-2,
				"array":   [
					true,
					false,
					null,
					1,
					-1.0e-2,
					["non-empty"],
					{
						"true":    true,
						"false":   false,
						"null":    null,
						"1":       1,
						"-1.0e-2": -1.0e-2,
						"array":   ["non-empty"],
						"object":  { "non": "empty" }
					}
				],
				"object": { "non": "empty" }
			}
		""" to mapOf(
			"true" to true,
			"false" to false,
			"null" to null,
			"1" to 1,
			"-1.0e-2" to -1.0e-2,
			"array" to listOf(
				true,
				false,
				null,
				1,
				-1.0e-2,
				listOf("non-empty"),
				mapOf(
					"true" to true,
					"false" to false,
					"null" to null,
					"1" to 1,
					"-1.0e-2" to -1.0e-2,
					"array" to listOf("non-empty"),
					"object" to mapOf("non" to "empty")
				)
			),
			"object" to mapOf("non" to "empty")
		)
	),
	encodableOnly = mapOf(
		mapOf(
			"true" to true,
			"false" to false,
			"null" to null,
			"1" to 1,
			"-1.0e-2" to -1.0e-2,
			"array" to listOf(
				true,
				false,
				null,
				1,
				-1.0e-2,
				listOf("non-empty"),
				mapOf(
					"true" to true,
					"false" to false,
					"null" to null,
					"1" to 1,
					"-1.0e-2" to -1.0e-2,
					"array" to listOf("non-empty"),
					"object" to mapOf("non" to "empty")
				)
			),
			"object" to mapOf("non" to "empty")
		) to """
			{
				"true":    true,
				"false":   false,
				"null":    null,
				"1":       1,
				"-1.0e-2": -0.01,
				"array":   [
					true,
					false,
					null,
					1,
					-0.01,
					["non-empty"],
					{
						"true":    true,
						"false":   false,
						"null":    null,
						"1":       1,
						"-1.0e-2": -0.01,
						"array":   ["non-empty"],
						"object":  { "non": "empty" }
					}
				],
				"object": { "non": "empty" }
			}
			""".filterNot(Char::isWhitespace)
	)
)
