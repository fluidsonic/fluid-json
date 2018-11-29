package examples

import com.github.fluidsonic.fluid.json.*
import java.io.StringWriter


fun main(args: Array<String>) {
	// You can also avoid the higher-order functions for stream serializing and write token by token
	val output = StringWriter()
	JSONWriter.build(output).use { writer ->
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
