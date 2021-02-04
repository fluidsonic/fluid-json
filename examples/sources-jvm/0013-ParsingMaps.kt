package examples

import io.fluidsonic.json.*


fun main() {
	val parser = JsonCodingParser.default

	// You can parse a Map of keys and values in a typesafe way
	val value1 = parser.parseValueOfType<Map<*, *>>("""{ "one": 1, "two": 2, "three": 3 }""")
	println(value1)

	// You can parse a Map of keys and values of a specific value type in a typesafe way
	val value2 = parser.parseValueOfType<Map<String, String?>>("""{ "one": "one", "two": "two", "three": null }""")
	println(value2)

	// Note that due to a limitation of Kotlin and the JVM you can specify either `Map<String,String>` or
	// `Map<String?,String?>` but the resulting map can always contain `null` keys and values. This can cause an
	// unexpected `NullPointerException` at runtime if the source data contains `null`s.
	val value3 = parser.parseValueOfType<Map<String, String>>("""{ "one": "one", "two": "two", "three": null }""")
	println(value3)
}
