package examples

import com.github.fluidsonic.fluid.json.JSONReader
import com.github.fluidsonic.fluid.json.JSONToken
import java.io.StringReader


fun main(args: Array<String>) {
	// you can also avoid the high-order functions for stream parsing and read token by token

	val input = StringReader("""{ "data": [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10] }""")
	JSONReader.build(input).use { reader ->
		reader.apply {
			readMapStart()
			println(readMapKey())

			readListStart()
			while (nextToken != JSONToken.listEnd) {
				println(readInt())
			}
			readListEnd()

			readMapEnd()
		}
	}
}
