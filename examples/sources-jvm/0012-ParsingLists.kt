package examples

import io.fluidsonic.json.*


fun main() {
	val parser = JsonCodingParser.default

	// You can parse a List of values in a typesafe way
	val value1 = parser.parseValueOfType<List<*>>("[1, true, []]")
	println(value1)

	// You can parse a List of values of a specific type in a typesafe way
	val value2 = parser.parseValueOfType<List<String?>>("""["one", "two", null]""")
	println(value2)

	// Note that due to a limitation of Kotlin and the JVM you can specify either `List<String>` or `List<String?>` but
	// the resulting list can always contain `null` values. This can cause an unexpected `NullPointerException` at
	// runtime if the source data contains `null`s.
	val value3 = parser.parseValueOfType<List<String>>("""["one", "two", null]""")
	println(value3)
}
