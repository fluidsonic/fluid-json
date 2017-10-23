package examples

import com.github.fluidsonic.fluid.json.JSONSerializer
import java.io.StringWriter


fun main(args: Array<String>) {
	val serializer = JSONSerializer.default()

	// you can also let the serializer write to a Writer
	val writer = StringWriter()
	serializer.serializeValue(mapOf(
		"hello" to "world",
		"test" to 123
	), destination = writer)

	println(writer.toString())
}
