package examples

import com.github.fluidsonic.fluid.json.*
import java.io.StringReader


fun main(args: Array<String>) {
	// You can read values directly from a stream without having to parse all at once
	val input = StringReader("""{ "data": [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10] }""")
	JSONReader.build(input).use { reader ->
		reader.readFromMapByElementValue { key ->
			println(key)

			readFromListByElement {
				println(readInt())
			}
		}
	}
}
