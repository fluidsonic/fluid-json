package examples

import com.github.fluidsonic.fluid.json.JSONParser
import com.github.fluidsonic.fluid.json.parseValue


fun main(args: Array<String>) {
	val parser = JSONParser.default()

	// given a JSON string you get a value of basic Kotlin types (String, Int, List, Map, etc.)
	val value = parser.parseValue("""
		{
			"hello": "world",
			"test":  123
		}
	""")

	println(value)
}
