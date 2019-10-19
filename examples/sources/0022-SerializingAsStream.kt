package examples

import io.fluidsonic.json.*
import java.io.*


fun main() {
	// You can write values directly to a stream without having to build the temporary map or list
	val output = StringWriter()
	JsonWriter.build(output).use { writer ->
		writer.writeIntoMap {
			writeMapElement("data") {
				writeIntoList {
					for (value in 0 .. 10) {
						writer.writeInt(value)
					}
				}
			}
		}
	}

	println(output.toString())
}
