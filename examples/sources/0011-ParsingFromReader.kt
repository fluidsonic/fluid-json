package examples

import io.fluidsonic.json.*
import java.io.*


fun main() {
	val parser = JsonParser.default

	// You can also let the parser read from a Reader
	val value = parser.parseValue(StringReader(""" { "hello": "world", "test": 123 } """))
	println(value)
}
