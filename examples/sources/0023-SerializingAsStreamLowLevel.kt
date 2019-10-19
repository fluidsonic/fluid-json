package examples

import io.fluidsonic.json.*
import java.io.*


fun main() {
	// You can also avoid the higher-order functions for stream serializing and write token by token
	val output = StringWriter()
	JsonWriter.build(output).use { writer ->
		writer.apply {
			writeMapStart()
			writeMapKey("data")

			writeListStart()
			for (value in 0 .. 10) {
				writeInt(value)
			}
			writeListEnd()

			writeMapEnd()
		}
	}

	println(output.toString())
}
