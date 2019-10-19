package examples

import io.fluidsonic.json.*


fun main() {
	val serializer = JsonSerializer.default

	// Given a value of a basic Kotlin type (String, Int, List, Map, etc.) you can get a JSON string
	val json = serializer.serializeValue(mapOf(
		"hello" to "world",
		"test" to 123
	))

	println(json)
}
