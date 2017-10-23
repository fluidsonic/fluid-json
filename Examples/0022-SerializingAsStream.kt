package examples

import com.github.fluidsonic.fluid.json.JSONWriter
import com.github.fluidsonic.fluid.json.writeIntoList
import com.github.fluidsonic.fluid.json.writeIntoMap
import com.github.fluidsonic.fluid.json.writeMapElement
import java.io.StringWriter


fun main(args: Array<String>) {
	// you can write values directly to a stream without having to build the temporary map or list

	val output = StringWriter()
	JSONWriter.build(output).use { writer ->
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
