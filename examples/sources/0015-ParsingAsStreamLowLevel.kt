package examples

import io.fluidsonic.json.*
import java.io.*


fun main() {
	// You can also avoid the higher-order functions for stream parsing and read token by token
	val input = StringReader("""{ "data": [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10] }""")
	JsonReader.build(input).use { reader ->
		reader.apply {
			readMapStart()
			println(readMapKey())

			readListStart()
			while (nextToken != JsonToken.listEnd) {
				println(readInt())
			}
			readListEnd()

			readMapEnd()
		}
	}
}
