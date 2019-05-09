package examples

import com.github.fluidsonic.fluid.json.*
import java.io.*


fun main() {
	val serializer = JSONSerializer.default

	// You can also let the serializer write to a Writer
	val writer = StringWriter()
	serializer.serializeValue(mapOf(
		"hello" to "world",
		"test" to 123
	), destination = writer)

	println(writer.toString())
}
