package examples

import com.github.fluidsonic.fluid.json.JSONNullability
import com.github.fluidsonic.fluid.json.JSONParser
import com.github.fluidsonic.fluid.json.parseList
import com.github.fluidsonic.fluid.json.parseListOfType


fun main(args: Array<String>) {
	val parser = JSONParser.default()

	// you can parse a List of non-nullable values in a typesafe way
	val example1: List<Any>? = parser.parseList("[1, true, []]")
	println(example1)

	// you can parse a List of non-nullable values of a specific type in a typesafe way
	val example2: List<String>? = parser.parseListOfType("""["one", "two", "three"]""")
	println(example2)

	// you can parse a List of nullable values in a typesafe way
	val example3: List<Any?>? = parser.parseList("[1, true, null]", JSONNullability.Value)
	println(example3)

	// you can parse a List of nullable values of a specific type in a typesafe way
	val example4: List<String?>? = parser.parseListOfType("""["one", "two", null]""", JSONNullability.Value)
	println(example4)
}
