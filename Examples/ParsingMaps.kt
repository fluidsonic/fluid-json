package examples

import com.github.fluidsonic.fluid.json.JSONNullability
import com.github.fluidsonic.fluid.json.JSONParser
import com.github.fluidsonic.fluid.json.parseMap
import com.github.fluidsonic.fluid.json.parseMapOfType


fun main(args: Array<String>) {
	val parser = JSONParser.default()

	// you can parse a Map of non-nullable keys and values in a typesafe way
	val example1: Map<String, Any>? = parser.parseMap("""{ "one": 1, "two": 2, "three": 3 }""")
	println(example1)

	// you can parse a Map of non-nullable keys and values of a specific value type in a typesafe way
	val example2: Map<String, String>? = parser.parseMapOfType("""{ "one": "one", "two": "two", "three": "three" }""")
	println(example2)

	// you can parse a Map of non-nullable keys and nullable values in a typesafe way
	val example3: Map<String, Any?>? = parser.parseMap("""{ "one": 1, "two": 2, "three": null }""", JSONNullability.Value)
	println(example3)

	// you can parse a Map of non-nullable keys and nullable values of a specific value type in a typesafe way
	val example4: Map<String, String?>? = parser.parseMapOfType("""{ "one": "one", "two": "two", "three": null }""", JSONNullability.Value)
	println(example4)
}
