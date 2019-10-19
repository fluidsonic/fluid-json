package examples

import io.fluidsonic.json.*
import java.io.*


fun main() {
	val serializer = JsonSerializer.default

	// You can also let the serializer write to a Writer
	val writer = StringWriter()
	serializer.serializeValue(mapOf(
		"hello" to "world",
		"test" to 123
	), destination = writer)

	println(writer.toString())
}
