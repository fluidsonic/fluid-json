package examples

import com.github.fluidsonic.fluid.json.JSONSerializer
import com.github.fluidsonic.fluid.json.serializeValue


fun main(args: Array<String>) {
	val serializer = JSONSerializer.default()

	// given a value of basic Kotlin types (String, Int, List, Map, etc.) you can get a JSON string
	val json = serializer.serializeValue(mapOf(
		"hello" to "world",
		"test" to 123
	))

	println(json)
}
