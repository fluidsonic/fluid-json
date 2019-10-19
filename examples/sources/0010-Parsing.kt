package examples

import io.fluidsonic.json.*


fun main() {
	val parser = JsonParser.default

	// Given a JSON string you get a value of basic Kotlin types (String, Int, List, Map, etc.)
	val value1 = parser.parseValue(""" { "hello": "world", "test": 123 } """)
	println(value1)

	// Many functions have `…OrNull` versions which allow a `null` value to be returned instead of causing a
	// `JsonException` in such cases.
	val value2 = parser.parseValueOrNull("null")
	println(value2)
}
