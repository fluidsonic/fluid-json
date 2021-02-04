package tests.coding


internal val stringData: TestData<String> = TestData(
	symmetric = mapOf(
		"" to "\"\"",
		"simple" to "\"simple\"",
		"emoji: 🐶" to "\"emoji: 🐶\"",
		"\\ \"" to "\"\\\\ \\\"\"",
		"\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\u000B\u000E\u000F\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001A\u001B\u001C\u001D\u001E\u001F\u0020" to "\"\\u0000\\u0001\\u0002\\u0003\\u0004\\u0005\\u0006\\u0007\\u000B\\u000E\\u000F\\u0010\\u0011\\u0012\\u0013\\u0014\\u0015\\u0016\\u0017\\u0018\\u0019\\u001A\\u001B\\u001C\\u001D\\u001E\\u001F\u0020\"",
		" \\ \" / \b \u000C \n \r \t " to "\" \\\\ \\\" / \\b \\f \\n \\r \\t \""
	),
	decodableOnly = mapOf(
		"\"\\u0022\"" to "\"",
		"\" \\\\ \\\" \\/ \\b \\f \\n \\r \\t \\uD83D\\udc36 \"" to " \\ \" / \b \u000C \n \r \t 🐶 "
	)
)
