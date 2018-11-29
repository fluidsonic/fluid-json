package examples

import com.github.fluidsonic.fluid.json.*


fun main(args: Array<String>) {
	val serializer = JSONSerializer.default

	// Given a value of a basic Kotlin type (String, Int, List, Map, etc.) you can get a JSON string
	val json = serializer.serializeValue(mapOf(
		"hello" to "world",
		"test" to 123
	))

	println(json)
}
