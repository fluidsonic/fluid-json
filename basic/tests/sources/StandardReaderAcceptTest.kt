package tests.basic

import ch.tutteli.atrium.api.cc.en_GB.*
import ch.tutteli.atrium.verbs.*
import io.fluidsonic.json.*
import org.junit.jupiter.api.*
import java.io.*
import kotlin.collections.set


internal class StandardReaderAcceptTest {


	@Test
	fun testClose() {
		reader("null").close()
		reader("null").apply {
			close()
			close()
		}
	}


	@Test
	fun testLargeInput() {
		reader(Resources.stream("large.json").reader()).apply {
			val value = readMap()

			val innerMap = (0 .. 100).associate { it.toString() to it }
			val outerMap = (0 .. 100).associate { it.toString() to innerMap }

			assert(value).toBe(outerMap)
		}
	}


	@Test
	fun testReadBoolean() {
		assert(reader("true").readBoolean()).toBe(true)
		assert(reader("false").readBoolean()).toBe(false)
	}


	@Test
	fun testBooleanOrNull() {
		assert(reader("true").readBooleanOrNull()).toBe(true)
		assert(reader("false").readBooleanOrNull()).toBe(false)
		assert(reader("null").readBooleanOrNull()).toBe(null)
	}


	@Test
	fun testByte() {
		assert(reader("0").readByte()).toBe(0.toByte())
		assert(reader("-0").readByte()).toBe(0.toByte())
		assert(reader("100").readByte()).toBe(100.toByte())
		assert(reader("-100").readByte()).toBe((-100).toByte())
		assert(reader("0.000").readByte()).toBe(0.toByte())
		assert(reader("-0.000").readByte()).toBe(0.toByte())
		assert(reader("100.001").readByte()).toBe(100.toByte())
		assert(reader("-100.001").readByte()).toBe((-100).toByte())
		assert(reader("100.999").readByte()).toBe(100.toByte())
		assert(reader("-100.999").readByte()).toBe((-100).toByte())
		assert(reader("1e2").readByte()).toBe(100.toByte())
		assert(reader("-1e2").readByte()).toBe((-100).toByte())
		assert(reader("1.0e2").readByte()).toBe(100.toByte())
		assert(reader("-1.0e2").readByte()).toBe((-100).toByte())
		assert(reader("1.0e+2").readByte()).toBe(100.toByte())
		assert(reader("-1.0e+2").readByte()).toBe((-100).toByte())
		assert(reader("1.0e-2").readByte()).toBe(0.toByte())
		assert(reader("-1.0e-2").readByte()).toBe(0.toByte())
	}

	@Test
	fun testByteOrNull() {
		assert(reader("0").readByteOrNull()).toBe(0.toByte())
		assert(reader("-0").readByteOrNull()).toBe(0.toByte())
		assert(reader("100").readByteOrNull()).toBe(100.toByte())
		assert(reader("-100").readByteOrNull()).toBe((-100).toByte())
		assert(reader("0.000").readByteOrNull()).toBe(0.toByte())
		assert(reader("-0.000").readByteOrNull()).toBe(0.toByte())
		assert(reader("100.001").readByteOrNull()).toBe(100.toByte())
		assert(reader("-100.001").readByteOrNull()).toBe((-100).toByte())
		assert(reader("100.999").readByteOrNull()).toBe(100.toByte())
		assert(reader("-100.999").readByteOrNull()).toBe((-100).toByte())
		assert(reader("1e2").readByteOrNull()).toBe(100.toByte())
		assert(reader("-1e2").readByteOrNull()).toBe((-100).toByte())
		assert(reader("1.0e2").readByteOrNull()).toBe(100.toByte())
		assert(reader("-1.0e2").readByteOrNull()).toBe((-100).toByte())
		assert(reader("1.0e+2").readByteOrNull()).toBe(100.toByte())
		assert(reader("-1.0e+2").readByteOrNull()).toBe((-100).toByte())
		assert(reader("1.0e-2").readByteOrNull()).toBe(0.toByte())
		assert(reader("-1.0e-2").readByteOrNull()).toBe(0.toByte())
		assert(reader("null").readByteOrNull()).toBe(null)
	}


	@Test
	fun testReadChar() {
		assert(reader(""""\u0000"""").readChar()).toBe(0.toChar())
		assert(reader(""""a"""").readChar()).toBe('a')
	}


	@Test
	fun testReadCharOrNull() {
		assert(reader(""""\u0000"""").readCharOrNull()).toBe(0.toChar())
		assert(reader(""""a"""").readCharOrNull()).toBe('a')
		assert(reader("null").readCharOrNull()).toBe(null)
	}


	@Test
	fun testReadDouble() {
		assert(reader("0").readDouble()).toBe(0.0)
		assert(reader("-0").readDouble()).toBe(-0.0)
		assert(reader("100").readDouble()).toBe(100.0)
		assert(reader("-100").readDouble()).toBe(-100.0)
		assert(reader("0.000").readDouble()).toBe(0.0)
		assert(reader("-0.000").readDouble()).toBe(-0.0)
		assert(reader("100.001").readDouble()).toBe(100.001)
		assert(reader("-100.001").readDouble()).toBe(-100.001)
		assert(reader("100.999").readDouble()).toBe(100.999)
		assert(reader("-100.999").readDouble()).toBe(-100.999)
		assert(reader("1e2").readDouble()).toBe(100.0)
		assert(reader("-1e2").readDouble()).toBe(-100.0)
		assert(reader("1.0e2").readDouble()).toBe(100.0)
		assert(reader("-1.0e2").readDouble()).toBe(-100.0)
		assert(reader("1.0e+2").readDouble()).toBe(100.0)
		assert(reader("-1.0e+2").readDouble()).toBe(-100.0)
		assert(reader("1.0e-2").readDouble()).toBe(0.01)
		assert(reader("-1.0e-2").readDouble()).toBe(-0.01)
		assert(reader("9223372036854775808").readDouble()).toBe(9223372036854775808.0)
		assert(reader("-9223372036854775809").readDouble()).toBe(-9223372036854775809.0)
		assert(reader("1000000000000000000000000000000").readDouble()).toBe(1000000000000000000000000000000.0)
		assert(reader("-1000000000000000000000000000000").readDouble()).toBe(-1000000000000000000000000000000.0)
		assert(reader("1e20000").readDouble()).toBe(Double.POSITIVE_INFINITY)
		assert(reader("-1e20000").readDouble()).toBe(Double.NEGATIVE_INFINITY)
	}


	@Test
	fun testReadDoubleOrNull() {
		assert(reader("0").readDoubleOrNull()).toBe(0.0)
		assert(reader("-0").readDoubleOrNull()).toBe(-0.0)
		assert(reader("100").readDoubleOrNull()).toBe(100.0)
		assert(reader("-100").readDoubleOrNull()).toBe(-100.0)
		assert(reader("0.000").readDoubleOrNull()).toBe(0.0)
		assert(reader("-0.000").readDoubleOrNull()).toBe(-0.0)
		assert(reader("100.001").readDoubleOrNull()).toBe(100.001)
		assert(reader("-100.001").readDoubleOrNull()).toBe(-100.001)
		assert(reader("100.999").readDoubleOrNull()).toBe(100.999)
		assert(reader("-100.999").readDoubleOrNull()).toBe(-100.999)
		assert(reader("1e2").readDoubleOrNull()).toBe(100.0)
		assert(reader("-1e2").readDoubleOrNull()).toBe(-100.0)
		assert(reader("1.0e2").readDoubleOrNull()).toBe(100.0)
		assert(reader("-1.0e2").readDoubleOrNull()).toBe(-100.0)
		assert(reader("1.0e+2").readDoubleOrNull()).toBe(100.0)
		assert(reader("-1.0e+2").readDoubleOrNull()).toBe(-100.0)
		assert(reader("1.0e-2").readDoubleOrNull()).toBe(0.01)
		assert(reader("-1.0e-2").readDoubleOrNull()).toBe(-0.01)
		assert(reader("9223372036854775808").readDoubleOrNull()).toBe(9223372036854775808.0)
		assert(reader("-9223372036854775809").readDoubleOrNull()).toBe(-9223372036854775809.0)
		assert(reader("1000000000000000000000000000000").readDoubleOrNull()).toBe(1000000000000000000000000000000.0)
		assert(reader("-1000000000000000000000000000000").readDoubleOrNull()).toBe(-1000000000000000000000000000000.0)
		assert(reader("1e20000").readDoubleOrNull()).toBe(Double.POSITIVE_INFINITY)
		assert(reader("-1e20000").readDoubleOrNull()).toBe(Double.NEGATIVE_INFINITY)
		assert(reader("null").readDoubleOrNull()).toBe(null)
	}


	@Test
	fun testReadFloat() {
		assert(reader("0").readFloat()).toBe(0.0f)
		assert(reader("-0").readFloat()).toBe(-0.0f)
		assert(reader("100").readFloat()).toBe(100.0f)
		assert(reader("-100").readFloat()).toBe(-100.0f)
		assert(reader("0.000").readFloat()).toBe(0.0f)
		assert(reader("-0.000").readFloat()).toBe(-0.0f)
		assert(reader("100.001").readFloat()).toBe(100.001f)
		assert(reader("-100.001").readFloat()).toBe(-100.001f)
		assert(reader("100.999").readFloat()).toBe(100.999f)
		assert(reader("-100.999").readFloat()).toBe(-100.999f)
		assert(reader("1e2").readFloat()).toBe(100.0f)
		assert(reader("-1e2").readFloat()).toBe(-100.0f)
		assert(reader("1.0e2").readFloat()).toBe(100.0f)
		assert(reader("-1.0e2").readFloat()).toBe(-100.0f)
		assert(reader("1.0e+2").readFloat()).toBe(100.0f)
		assert(reader("-1.0e+2").readFloat()).toBe(-100.0f)
		assert(reader("1.0e-2").readFloat()).toBe(0.01f)
		assert(reader("-1.0e-2").readFloat()).toBe(-0.01f)
		assert(reader("9223372036854775808").readFloat()).toBe(9223372036854775808.0f)
		assert(reader("-9223372036854775809").readFloat()).toBe(-9223372036854775809.0f)
		assert(reader("1000000000000000000000000000000").readFloat()).toBe(1000000000000000000000000000000.0f)
		assert(reader("-1000000000000000000000000000000").readFloat()).toBe(-1000000000000000000000000000000.0f)
		assert(reader("1e20000").readFloat()).toBe(Float.POSITIVE_INFINITY)
		assert(reader("-1e20000").readFloat()).toBe(Float.NEGATIVE_INFINITY)
	}


	@Test
	fun testReadFloatOrNull() {
		assert(reader("0").readFloatOrNull()).toBe(0.0f)
		assert(reader("-0").readFloatOrNull()).toBe(-0.0f)
		assert(reader("100").readFloatOrNull()).toBe(100.0f)
		assert(reader("-100").readFloatOrNull()).toBe(-100.0f)
		assert(reader("0.000").readFloatOrNull()).toBe(0.0f)
		assert(reader("-0.000").readFloatOrNull()).toBe(-0.0f)
		assert(reader("100.001").readFloatOrNull()).toBe(100.001f)
		assert(reader("-100.001").readFloatOrNull()).toBe(-100.001f)
		assert(reader("100.999").readFloatOrNull()).toBe(100.999f)
		assert(reader("-100.999").readFloatOrNull()).toBe(-100.999f)
		assert(reader("1e2").readFloatOrNull()).toBe(100.0f)
		assert(reader("-1e2").readFloatOrNull()).toBe(-100.0f)
		assert(reader("1.0e2").readFloatOrNull()).toBe(100.0f)
		assert(reader("-1.0e2").readFloatOrNull()).toBe(-100.0f)
		assert(reader("1.0e+2").readFloatOrNull()).toBe(100.0f)
		assert(reader("-1.0e+2").readFloatOrNull()).toBe(-100.0f)
		assert(reader("1.0e-2").readFloatOrNull()).toBe(0.01f)
		assert(reader("-1.0e-2").readFloatOrNull()).toBe(-0.01f)
		assert(reader("9223372036854775808").readFloatOrNull()).toBe(9223372036854775808.0f)
		assert(reader("-9223372036854775809").readFloatOrNull()).toBe(-9223372036854775809.0f)
		assert(reader("1000000000000000000000000000000").readFloatOrNull()).toBe(1000000000000000000000000000000.0f)
		assert(reader("-1000000000000000000000000000000").readFloatOrNull()).toBe(-1000000000000000000000000000000.0f)
		assert(reader("1e20000").readFloatOrNull()).toBe(Float.POSITIVE_INFINITY)
		assert(reader("-1e20000").readFloatOrNull()).toBe(Float.NEGATIVE_INFINITY)
		assert(reader("null").readFloatOrNull()).toBe(null)
	}


	@Test
	fun testReadFromList() {
		reader("[]").apply {
			assert(readFromList { Dummy1 }).toBe(Dummy1)
		}

		reader("[ \t\n\r]").apply {
			assert(readFromList { Dummy1 }).toBe(Dummy1)
		}

		reader("[1]").apply {
			assert(readFromList {
				skipValue()
				Dummy1
			}).toBe(Dummy1)
		}

		reader("[ true, \"hey\", null ]").apply {
			assert(readFromList {
				skipValue()
				skipValue()
				skipValue()
				Dummy1
			}).toBe(Dummy1)
		}

		reader("[ [], [ 1 ] ]").apply {
			assert(readFromList {
				assert(readFromList { Dummy2 }).toBe(Dummy2)
				assert(readFromList {
					skipValue()
					Dummy3
				}).toBe(Dummy3)
				Dummy1
			}).toBe(Dummy1)
		}
	}


	@Test
	fun testReadFromListByElement() {
		reader("[1,2]").apply {
			val list = mutableListOf<Int>()
			readFromListByElement { list += readInt() }
			assert(list).toBe(listOf(1, 2))
		}
	}


	@Test
	fun testReadFromMapInline() {
		reader("{}").apply {
			assert(readFromMap { Dummy1 }).toBe(Dummy1)
		}
		reader("{ \t\n\r}").apply {
			assert(readFromMap { Dummy1 }).toBe(Dummy1)
		}
		reader("{\"key\":1}").apply {
			assert(readFromMap {
				assert(readString()).toBe("key")
				assert(readInt()).toBe(1)
				Dummy1
			}).toBe(Dummy1)
		}
		reader("{ \"key0\": true, \"key1\" :\"hey\", \"key2\" : null }").apply {
			assert(readFromMap {
				assert(readString()).toBe("key0")
				assert(readBoolean()).toBe(true)
				assert(readString()).toBe("key1")
				assert(readString()).toBe("hey")
				assert(readString()).toBe("key2")
				readNull()
				Dummy1
			}).toBe(Dummy1)
		}
		reader("{ \"key0\": {}, \"key1\": { \"key\": 1 } }").apply {
			assert(readFromMap {
				assert(readString()).toBe("key0")
				assert(readFromMap { Dummy2 }).toBe(Dummy2)
				assert(readString()).toBe("key1")
				assert(readFromMap {
					assert(readString()).toBe("key")
					assert(readInt()).toBe(1)
					Dummy3
				}).toBe(Dummy3)
				Dummy1
			}).toBe(Dummy1)
		}
		reader("{ \" \\\\ \\\" \\/ \\b \\f \\n \\r \\t \\uD83D\\udc36 \": 1 }").apply {
			assert(readFromMap {
				assert(readString()).toBe(" \\ \" / \b \u000C \n \r \t 🐶 ")
				assert(readInt()).toBe(1)
				Dummy1
			}).toBe(Dummy1)
		}
		reader("{ \"0\": 0, \"2\": 2, \"1\": 1, \"3\": 3, \"-1\": -1 }").apply {
			assert(readFromMap {
				assert(readString()).toBe("0")
				assert(readInt()).toBe(0)
				assert(readString()).toBe("2")
				assert(readInt()).toBe(2)
				assert(readString()).toBe("1")
				assert(readInt()).toBe(1)
				assert(readString()).toBe("3")
				assert(readInt()).toBe(3)
				assert(readString()).toBe("-1")
				assert(readInt()).toBe(-1)
				Dummy1
			}).toBe(Dummy1)
		}
	}


	@Test
	fun testReadFromMapByElement() {
		reader("""{ "one": 1, "two": 2 }""").apply {
			val map = mutableMapOf<String, Int>()
			readFromMapByElement { map[readMapKey()] = readInt() }
			assert(map).toBe(mapOf("one" to 1, "two" to 2))
		}
	}


	@Test
	fun testReadFromMapByElementValue() {
		reader("""{ "one": 1, "two": 2 }""").apply {
			val map = mutableMapOf<String, Int>()
			readFromMapByElementValue { key -> map[key] = readInt() }
			assert(map).toBe(mapOf("one" to 1, "two" to 2))
		}
	}


	@Test
	fun testReadInt() {
		assert(reader("0").readInt()).toBe(0)
		assert(reader("-0").readInt()).toBe(0)
		assert(reader("100").readInt()).toBe(100)
		assert(reader("-100").readInt()).toBe(-100)
		assert(reader("0.000").readInt()).toBe(0)
		assert(reader("-0.000").readInt()).toBe(0)
		assert(reader("100.001").readInt()).toBe(100)
		assert(reader("-100.001").readInt()).toBe(-100)
		assert(reader("100.999").readInt()).toBe(100)
		assert(reader("-100.999").readInt()).toBe(-100)
		assert(reader("1e2").readInt()).toBe(100)
		assert(reader("-1e2").readInt()).toBe(-100)
		assert(reader("1.0e+2").readInt()).toBe(100)
		assert(reader("-1.0e+2").readInt()).toBe(-100)
		assert(reader("1.0e2").readInt()).toBe(100)
		assert(reader("-1.0e2").readInt()).toBe(-100)
		assert(reader("1.0e-2").readInt()).toBe(0)
		assert(reader("-1.0e-2").readInt()).toBe(0)
	}


	@Test
	fun testReadIntOrNull() {
		assert(reader("0").readIntOrNull()).toBe(0)
		assert(reader("-0").readIntOrNull()).toBe(0)
		assert(reader("100").readIntOrNull()).toBe(100)
		assert(reader("-100").readIntOrNull()).toBe(-100)
		assert(reader("0.000").readIntOrNull()).toBe(0)
		assert(reader("-0.000").readIntOrNull()).toBe(0)
		assert(reader("100.001").readIntOrNull()).toBe(100)
		assert(reader("-100.001").readIntOrNull()).toBe(-100)
		assert(reader("100.999").readIntOrNull()).toBe(100)
		assert(reader("-100.999").readIntOrNull()).toBe(-100)
		assert(reader("1e2").readIntOrNull()).toBe(100)
		assert(reader("-1e2").readIntOrNull()).toBe(-100)
		assert(reader("1.0e2").readIntOrNull()).toBe(100)
		assert(reader("-1.0e2").readIntOrNull()).toBe(-100)
		assert(reader("1.0e+2").readIntOrNull()).toBe(100)
		assert(reader("-1.0e+2").readIntOrNull()).toBe(-100)
		assert(reader("1.0e-2").readIntOrNull()).toBe(0)
		assert(reader("-1.0e-2").readIntOrNull()).toBe(0)
		assert(reader("null").readIntOrNull()).toBe(null)
	}


	@Test
	fun testReadList() {
		assert(reader("[]").readList()).toBe(emptyList())
		assert(reader("[ \t\n\r]").readList()).toBe(emptyList())
		assert(reader("[1]").readList()).toBe(listOf(1))
		assert(reader("[ true, \"hey\", null ]").readList()).toBe(listOf(true, "hey", null))
		assert(reader("[ [], [ 1 ] ]").readList()).toBe(listOf(emptyList<Any?>(), listOf(1)))
	}


	@Test
	fun testReadListByElement() {
		assert(reader("[ true, \"hey\", null ]").readListByElement { readValueOrNull() })
			.toBe(listOf(true, "hey", null))
	}


	@Test
	fun testReadListEnd() {
		reader("[]").apply {
			readListStart()
			assert(nextToken).toBe(JsonToken.listEnd)
			readListEnd()
			assert(nextToken).toBe(null)
		}
	}


	@Test
	fun testReadListOrNull() {
		assert(reader("[]").readListOrNull()).toBe(emptyList())
		assert(reader("[ \t\n\r]").readListOrNull()).toBe(emptyList())
		assert(reader("[1]").readListOrNull()).toBe(listOf(1))
		assert(reader("[ true, \"hey\", null ]").readListOrNull()).toBe(listOf(true, "hey", null))
		assert(reader("[ [], [ 1 ] ]").readListOrNull()).toBe(listOf(emptyList<Any?>(), listOf(1)))
		assert(reader("null").readListOrNull()).toBe(null)
	}


	@Test
	fun testReadListOrNullByElement() {
		assert(reader("[ true, \"hey\", null ]").readListOrNullByElement { readValueOrNull() })
			.toBe(listOf(true, "hey", null))

		assert(reader("null").readListOrNullByElement { readValueOrNull() })
			.toBe(null)
	}


	@Test
	fun testReadListStart() {
		reader("[]").apply {
			assert(nextToken).toBe(JsonToken.listStart)
			readListStart()
			assert(nextToken).toBe(JsonToken.listEnd)
		}
	}


	@Test
	fun testReadLong() {
		assert(reader("0").readLong()).toBe(0L)
		assert(reader("-0").readLong()).toBe(0L)
		assert(reader("100").readLong()).toBe(100L)
		assert(reader("-100").readLong()).toBe(-100L)
		assert(reader("0.000").readLong()).toBe(0L)
		assert(reader("-0.000").readLong()).toBe(0L)
		assert(reader("100.001").readLong()).toBe(100L)
		assert(reader("-100.001").readLong()).toBe(-100L)
		assert(reader("100.999").readLong()).toBe(100L)
		assert(reader("-100.999").readLong()).toBe(-100L)
		assert(reader("1e2").readLong()).toBe(100L)
		assert(reader("-1e2").readLong()).toBe(-100L)
		assert(reader("1.0e2").readLong()).toBe(100L)
		assert(reader("-1.0e2").readLong()).toBe(-100L)
		assert(reader("1.0e+2").readLong()).toBe(100L)
		assert(reader("-1.0e+2").readLong()).toBe(-100L)
		assert(reader("1.0e-2").readLong()).toBe(0L)
		assert(reader("-1.0e-2").readLong()).toBe(0L)
	}


	@Test
	fun testReadLongOrNull() {
		assert(reader("0").readLongOrNull()).toBe(0L)
		assert(reader("-0").readLongOrNull()).toBe(0L)
		assert(reader("100").readLongOrNull()).toBe(100L)
		assert(reader("-100").readLongOrNull()).toBe(-100L)
		assert(reader("0.000").readLongOrNull()).toBe(0L)
		assert(reader("-0.000").readLongOrNull()).toBe(0L)
		assert(reader("100.001").readLongOrNull()).toBe(100L)
		assert(reader("-100.001").readLongOrNull()).toBe(-100L)
		assert(reader("100.999").readLongOrNull()).toBe(100L)
		assert(reader("-100.999").readLongOrNull()).toBe(-100L)
		assert(reader("1e2").readLongOrNull()).toBe(100L)
		assert(reader("-1e2").readLongOrNull()).toBe(-100L)
		assert(reader("1.0e2").readLongOrNull()).toBe(100L)
		assert(reader("-1.0e2").readLongOrNull()).toBe(-100L)
		assert(reader("1.0e+2").readLongOrNull()).toBe(100L)
		assert(reader("-1.0e+2").readLongOrNull()).toBe(-100L)
		assert(reader("1.0e-2").readLongOrNull()).toBe(0L)
		assert(reader("-1.0e-2").readLongOrNull()).toBe(0L)
		assert(reader("null").readLongOrNull()).toBe(null)
	}


	@Test
	fun testReadMap() {
		assert(reader("{}").readMap()).toBe(emptyMap())
		assert(reader("{ \t\n\r}").readMap()).toBe(emptyMap())
		assert(reader("{\"key\":1}").readMap()).toBe(mapOf("key" to 1))
		assert(reader("{ \"key0\": true, \"key1\" :\"hey\", \"key2\" : null }").readMap()).toBe(mapOf(
			"key0" to true,
			"key1" to "hey",
			"key2" to null
		))
		assert(reader("{ \"key0\": {}, \"key1\": { \"key\": 1 } }").readMap()).toBe(mapOf(
			"key0" to emptyMap<String, Any>(),
			"key1" to mapOf("key" to 1)
		))
		assert(reader("{ \" \\\\ \\\" \\/ \\b \\f \\n \\r \\t \\uD83D\\udc36 \": 1 }").readMap()).toBe(mapOf(
			" \\ \" / \b \u000C \n \r \t 🐶 " to 1
		))
		assert(reader("{ \"0\": 0, \"2\": 2, \"1\": 1, \"3\": 3, \"-1\": -1 }").readMap().toList()).toBe(listOf(
			"0" to 0,
			"2" to 2,
			"1" to 1,
			"3" to 3,
			"-1" to -1
		))
	}


	@Test
	fun testReadMapByElement() {
		assert(
			reader("{ \"key0\": true, \"key1\" :\"hey\", \"key2\" : null }")
				.readMapByElement { readMapKey() to readValueOrNull() }
		)
			.toBe(mapOf(
				"key0" to true,
				"key1" to "hey",
				"key2" to null
			))
	}


	@Test
	fun testReadMapByElementValue() {
		assert(
			reader("{ \"key0\": true, \"key1\" :\"hey\", \"key2\" : null }")
				.readMapByElementValue { readValueOrNull() }
		)
			.toBe(mapOf(
				"key0" to true,
				"key1" to "hey",
				"key2" to null
			))
	}


	@Test
	fun testReadMapEnd() {
		reader("{}").apply {
			readMapStart()
			assert(nextToken).toBe(JsonToken.mapEnd)
			readMapEnd()
			assert(nextToken).toBe(null)
		}
	}


	@Test
	fun testReadMapKey() {
		reader("{\"\"").apply {
			readMapStart()
			assert(readMapKey()).toBe("")
		}
		reader("{\"simple\"").apply {
			readMapStart()
			assert(readMapKey()).toBe("simple")
		}
		reader("{\" a bit longer \"").apply {
			readMapStart()
			assert(readMapKey()).toBe(" a bit longer ")
		}
		reader("{\"a dog: 🐶\"").apply {
			readMapStart()
			assert(readMapKey()).toBe("a dog: 🐶")
		}
		reader("{\" \\\\ \\\" \\/ \\b \\f \\n \\r \\t \\uD83D\\udc36 \"").apply {
			readMapStart()
			assert(readMapKey()).toBe(" \\ \" / \b \u000C \n \r \t 🐶 ")
		}
	}


	@Test
	fun testReadMapOrNull() {
		assert(reader("{}").readMapOrNull()).toBe(emptyMap())
		assert(reader("{ \t\n\r}").readMapOrNull()).toBe(emptyMap())
		assert(reader("{\"key\":1}").readMapOrNull()).toBe(mapOf("key" to 1))
		assert(reader("{ \"key0\": true, \"key1\" :\"hey\", \"key2\" : null }").readMapOrNull()).toBe(mapOf(
			"key0" to true,
			"key1" to "hey",
			"key2" to null
		))
		assert(reader("{ \"key0\": {}, \"key1\": { \"key\": 1 } }").readMapOrNull()).toBe(mapOf(
			"key0" to emptyMap<String, Any>(),
			"key1" to mapOf("key" to 1)
		))
		assert(reader("{ \" \\\\ \\\" \\/ \\b \\f \\n \\r \\t \\uD83D\\udc36 \": 1 }").readMapOrNull()).toBe(mapOf(
			" \\ \" / \b \u000C \n \r \t 🐶 " to 1
		))
		assert(reader("{ \"0\": 0, \"2\": 2, \"1\": 1, \"3\": 3, \"-1\": -1 }").readMapOrNull()?.toList()).toBe(listOf(
			"0" to 0,
			"2" to 2,
			"1" to 1,
			"3" to 3,
			"-1" to -1
		))
		assert(reader("null").readMapOrNull()).toBe(null)
	}


	@Test
	fun testReadMapOrNullByElement() {
		assert(
			reader("{ \"key0\": true, \"key1\" :\"hey\", \"key2\" : null }")
				.readMapOrNullByElement { readMapKey() to readValueOrNull() }
		)
			.toBe(mapOf(
				"key0" to true,
				"key1" to "hey",
				"key2" to null
			))

		assert(
			reader("null")
				.readMapOrNullByElement { readMapKey() to readValueOrNull() }
		)
			.toBe(null)
	}


	@Test
	fun testReadMapOrNullByElementValue() {
		assert(
			reader("{ \"key0\": true, \"key1\" :\"hey\", \"key2\" : null }")
				.readMapOrNullByElementValue { readValueOrNull() }
		)
			.toBe(mapOf(
				"key0" to true,
				"key1" to "hey",
				"key2" to null
			))

		assert(
			reader("null")
				.readMapOrNullByElementValue { readValueOrNull() }
		)
			.toBe(null)
	}


	@Test
	fun testReadMapStart() {
		reader("{}").apply {
			assert(nextToken).toBe(JsonToken.mapStart)
			readMapStart()
			assert(nextToken).toBe(JsonToken.mapEnd)
		}
	}


	@Test
	fun testReadNull() {
		assert((reader("null").readNull() as Any?)).toBe(null)
	}


	@Test
	fun testReadNumber() {
		assert(reader("0").readNumber()).toBe(0)
		assert(reader("-0").readNumber()).toBe(0)
		assert(reader("100").readNumber()).toBe(100)
		assert(reader("-100").readNumber()).toBe(-100)
		assert(reader("0.000").readNumber()).toBe(0.0)
		assert(reader("-0.000").readNumber()).toBe(-0.0)
		assert(reader("100.001").readNumber()).toBe(100.001)
		assert(reader("-100.001").readNumber()).toBe(-100.001)
		assert(reader("100.999").readNumber()).toBe(100.999)
		assert(reader("-100.999").readNumber()).toBe(-100.999)
		assert(reader("2147483647").readNumber()).toBe(2147483647)
		assert(reader("-2147483648").readNumber()).toBe(-2147483648)
		assert(reader("2147483648").readNumber()).toBe(2147483648L)
		assert(reader("-2147483649").readNumber()).toBe(-2147483649L)
		assert(reader("2147483647").readNumber()).toBe(2147483647)
		assert(reader("-2147483648").readNumber()).toBe(-2147483648)
		assert(reader("9223372036854775807").readNumber()).toBe(9223372036854775807L)
		assert(reader("-9223372036854775808").readNumber()).toBe(-9223372036854775807L - 1) // https://youtrack.jetbrains.com/issue/KT-17172
		assert(reader("9223372036854775808").readNumber()).toBe(9223372036854775808.0)
		assert(reader("-9223372036854775809").readNumber()).toBe(-9223372036854775809.0)
		assert(reader("1e2").readNumber()).toBe(100.0)
		assert(reader("-1e2").readNumber()).toBe(-100.0)
		assert(reader("1.0e2").readNumber()).toBe(100.0)
		assert(reader("-1.0e2").readNumber()).toBe(-100.0)
		assert(reader("1.0e-2").readNumber()).toBe(0.01)
		assert(reader("-1.0e-2").readNumber()).toBe(-0.01)
		assert(reader("1000000000000000000000000000000").readNumber()).toBe(1000000000000000000000000000000.0)
		assert(reader("-1000000000000000000000000000000").readNumber()).toBe(-1000000000000000000000000000000.0)
		assert(reader("1e20000").readNumber()).toBe(Double.POSITIVE_INFINITY)
		assert(reader("-1e20000").readNumber()).toBe(Double.NEGATIVE_INFINITY)
	}


	@Test
	fun testReadNumberOrNull() {
		assert(reader("0").readNumberOrNull()).toBe(0)
		assert(reader("-0").readNumberOrNull()).toBe(0)
		assert(reader("100").readNumberOrNull()).toBe(100)
		assert(reader("-100").readNumberOrNull()).toBe(-100)
		assert(reader("0.000").readNumberOrNull()).toBe(0.0)
		assert(reader("-0.000").readNumberOrNull()).toBe(-0.0)
		assert(reader("100.001").readNumberOrNull()).toBe(100.001)
		assert(reader("-100.001").readNumberOrNull()).toBe(-100.001)
		assert(reader("100.999").readNumberOrNull()).toBe(100.999)
		assert(reader("-100.999").readNumberOrNull()).toBe(-100.999)
		assert(reader("2147483647").readNumberOrNull()).toBe(2147483647)
		assert(reader("-2147483648").readNumberOrNull()).toBe(-2147483648)
		assert(reader("2147483648").readNumberOrNull()).toBe(2147483648L)
		assert(reader("-2147483649").readNumberOrNull()).toBe(-2147483649L)
		assert(reader("2147483647").readNumberOrNull()).toBe(2147483647)
		assert(reader("-2147483648").readNumberOrNull()).toBe(-2147483648)
		assert(reader("9223372036854775807").readNumberOrNull()).toBe(9223372036854775807L)
		assert(reader("-9223372036854775808").readNumberOrNull()).toBe(-9223372036854775807L - 1) // https://youtrack.jetbrains.com/issue/KT-17172
		assert(reader("9223372036854775808").readNumberOrNull()).toBe(9223372036854775808.0)
		assert(reader("-9223372036854775809").readNumberOrNull()).toBe(-9223372036854775809.0)
		assert(reader("1e2").readNumberOrNull()).toBe(100.0)
		assert(reader("-1e2").readNumberOrNull()).toBe(-100.0)
		assert(reader("1.0e2").readNumberOrNull()).toBe(100.0)
		assert(reader("-1.0e2").readNumberOrNull()).toBe(-100.0)
		assert(reader("1.0e-2").readNumberOrNull()).toBe(0.01)
		assert(reader("-1.0e-2").readNumberOrNull()).toBe(-0.01)
		assert(reader("1000000000000000000000000000000").readNumberOrNull()).toBe(1000000000000000000000000000000.0)
		assert(reader("-1000000000000000000000000000000").readNumberOrNull()).toBe(-1000000000000000000000000000000.0)
		assert(reader("1e20000").readNumberOrNull()).toBe(Double.POSITIVE_INFINITY)
		assert(reader("-1e20000").readNumberOrNull()).toBe(Double.NEGATIVE_INFINITY)
		assert(reader("null").readNumberOrNull()).toBe(null)
	}


	@Test
	fun testReadShort() {
		assert(reader("0").readShort()).toBe(0.toShort())
		assert(reader("-0").readShort()).toBe(0.toShort())
		assert(reader("100").readShort()).toBe(100.toShort())
		assert(reader("-100").readShort()).toBe((-100).toShort())
		assert(reader("0.000").readShort()).toBe(0.toShort())
		assert(reader("-0.000").readShort()).toBe(0.toShort())
		assert(reader("100.001").readShort()).toBe(100.toShort())
		assert(reader("-100.001").readShort()).toBe((-100).toShort())
		assert(reader("100.999").readShort()).toBe(100.toShort())
		assert(reader("-100.999").readShort()).toBe((-100).toShort())
		assert(reader("1e2").readShort()).toBe(100.toShort())
		assert(reader("-1e2").readShort()).toBe((-100).toShort())
		assert(reader("1.0e2").readShort()).toBe(100.toShort())
		assert(reader("-1.0e2").readShort()).toBe((-100).toShort())
		assert(reader("1.0e+2").readShort()).toBe(100.toShort())
		assert(reader("-1.0e+2").readShort()).toBe((-100).toShort())
		assert(reader("1.0e-2").readShort()).toBe(0.toShort())
		assert(reader("-1.0e-2").readShort()).toBe(0.toShort())
	}


	@Test
	fun testReadShortOrNull() {
		assert(reader("0").readShortOrNull()).toBe(0.toShort())
		assert(reader("-0").readShortOrNull()).toBe(0.toShort())
		assert(reader("100").readShortOrNull()).toBe(100.toShort())
		assert(reader("-100").readShortOrNull()).toBe((-100).toShort())
		assert(reader("0.000").readShortOrNull()).toBe(0.toShort())
		assert(reader("-0.000").readShortOrNull()).toBe(0.toShort())
		assert(reader("100.001").readShortOrNull()).toBe(100.toShort())
		assert(reader("-100.001").readShortOrNull()).toBe((-100).toShort())
		assert(reader("100.999").readShortOrNull()).toBe(100.toShort())
		assert(reader("-100.999").readShortOrNull()).toBe((-100).toShort())
		assert(reader("1e2").readShortOrNull()).toBe(100.toShort())
		assert(reader("-1e2").readShortOrNull()).toBe((-100).toShort())
		assert(reader("1.0e2").readShortOrNull()).toBe(100.toShort())
		assert(reader("-1.0e2").readShortOrNull()).toBe((-100).toShort())
		assert(reader("1.0e+2").readShortOrNull()).toBe(100.toShort())
		assert(reader("-1.0e+2").readShortOrNull()).toBe((-100).toShort())
		assert(reader("1.0e-2").readShortOrNull()).toBe(0.toShort())
		assert(reader("-1.0e-2").readShortOrNull()).toBe(0.toShort())
		assert(reader("null").readShortOrNull()).toBe(null)
	}


	@Test
	fun testReadString() {
		assert(reader("\"\"").readString()).toBe("")
		assert(reader("\"\\u0022\"").readString()).toBe("\"")
		assert(reader("\"simple\"").readString()).toBe("simple")
		assert(reader("\" a bit longer \"").readString()).toBe(" a bit longer ")
		assert(reader("\"a dog: 🐶\"").readString()).toBe("a dog: 🐶")
		assert(reader("\" \\\\ \\\" \\/ \\b \\f \\n \\r \\t \\uD83D\\udc36 \"").readString()).toBe(" \\ \" / \b \u000C \n \r \t 🐶 ")
		reader("{\"key\"").apply { readMapStart();assert(readString()).toBe("key") }
	}


	@Test
	fun testReadStringOrNull() {
		assert(reader("\"\"").readStringOrNull()).toBe("")
		assert(reader("\"\\u0022\"").readStringOrNull()).toBe("\"")
		assert(reader("\"simple\"").readStringOrNull()).toBe("simple")
		assert(reader("\" a bit longer \"").readStringOrNull()).toBe(" a bit longer ")
		assert(reader("\"a dog: 🐶\"").readStringOrNull()).toBe("a dog: 🐶")
		assert(reader("\" \\\\ \\\" \\/ \\b \\f \\n \\r \\t \\uD83D\\udc36 \"").readStringOrNull()).toBe(" \\ \" / \b \u000C \n \r \t 🐶 ")
		assert(reader("null").readStringOrNull()).toBe(null)
	}


	@Test
	fun testReadValue() {
		reader("[true, 0, [], {}, \"\", {\"\":true}]").apply {
			readFromList {
				assert(readValue()).toBe(true)
				assert(readValue()).toBe(0)
				assert(readValue()).toBe(emptyList<Any?>())
				assert(readValue()).toBe(emptyMap<String, Any?>())
				assert(readValue()).toBe("")
				readFromMap {
					assert(readValue()).toBe("")
					assert(readValue()).toBe(true)
				}
			}
		}
	}


	@Test
	fun testReadValueOrNull() {
		reader("[null, true, 0, [], {}, \"\", {\"\":true}]").apply {
			readFromList {
				assert(readValueOrNull()).toBe(null)
				assert(readValueOrNull()).toBe(true)
				assert(readValueOrNull()).toBe(0)
				assert(readValueOrNull()).toBe(emptyList<Any?>())
				assert(readValueOrNull()).toBe(emptyMap<String, Any?>())
				assert(readValueOrNull()).toBe("")
				readFromMap {
					assert(readValueOrNull()).toBe("")
					assert(readValueOrNull()).toBe(true)
				}
			}
		}
	}

	@Test
	fun testSkipValue() {
		reader("[null, 0, [], {}, \"\", {\"\":true}]").apply {
			readFromList {
				assert(nextToken).toBe(JsonToken.nullValue)
				skipValue()
				assert(nextToken).toBe(JsonToken.numberValue)
				skipValue()
				assert(nextToken).toBe(JsonToken.listStart)
				skipValue()
				assert(nextToken).toBe(JsonToken.mapStart)
				skipValue()
				assert(nextToken).toBe(JsonToken.stringValue)
				skipValue()
				assert(nextToken).toBe(JsonToken.mapStart)
				readFromMap {
					assert(nextToken).toBe(JsonToken.mapKey)
					skipValue()
					assert(nextToken).toBe(JsonToken.booleanValue)
					skipValue()
					assert(nextToken).toBe(JsonToken.mapEnd)
				}
			}
		}
	}


	@Test
	fun testTerminate() {
		reader("null").apply {
			readNull()
			terminate()
		}
	}


	@Nested
	inner class NextTokenTest {

		@Test
		fun testMatchesFullToken() {
			assert(reader("null").nextToken).toBe(JsonToken.nullValue)
			assert(reader("true").nextToken).toBe(JsonToken.booleanValue)
			assert(reader("false").nextToken).toBe(JsonToken.booleanValue)
			assert(reader("0").nextToken).toBe(JsonToken.numberValue)
			assert(reader("1").nextToken).toBe(JsonToken.numberValue)
			assert(reader("-1.0").nextToken).toBe(JsonToken.numberValue)
			assert(reader("\"\"").nextToken).toBe(JsonToken.stringValue)
			reader("[]").apply {
				assert(nextToken).toBe(JsonToken.listStart)
				readListStart()
				assert(nextToken).toBe(JsonToken.listEnd)
			}
			reader("{}").apply {
				assert(nextToken).toBe(JsonToken.mapStart)
				readMapStart()
				assert(nextToken).toBe(JsonToken.mapEnd)
			}
		}


		@Test
		fun testConsumesMinimalInput() {
			assert(reader("n").nextToken).toBe(JsonToken.nullValue)
			assert(reader("t").nextToken).toBe(JsonToken.booleanValue)
			assert(reader("f").nextToken).toBe(JsonToken.booleanValue)
			assert(reader("0").nextToken).toBe(JsonToken.numberValue)
			assert(reader("1").nextToken).toBe(JsonToken.numberValue)
			assert(reader("-").nextToken).toBe(JsonToken.numberValue)
			assert(reader("[").nextToken).toBe(JsonToken.listStart)
			assert(reader("\"").nextToken).toBe(JsonToken.stringValue)
			assert(reader("{").nextToken).toBe(JsonToken.mapStart)
		}


		@Test
		fun testIdempotency() {
			reader("n").apply { nextToken;assert(nextToken).toBe(JsonToken.nullValue) }
			reader("t").apply { nextToken;assert(nextToken).toBe(JsonToken.booleanValue) }
			reader("f").apply { nextToken;assert(nextToken).toBe(JsonToken.booleanValue) }
			reader("0").apply { nextToken;assert(nextToken).toBe(JsonToken.numberValue) }
			reader("1").apply { nextToken;assert(nextToken).toBe(JsonToken.numberValue) }
			reader("-").apply { nextToken;assert(nextToken).toBe(JsonToken.numberValue) }
			reader("[").apply { nextToken;assert(nextToken).toBe(JsonToken.listStart) }
			reader("\"").apply { nextToken;assert(nextToken).toBe(JsonToken.stringValue) }
			reader("{").apply { nextToken;assert(nextToken).toBe(JsonToken.mapStart) }
		}


		@Test
		fun testCompletesWithNull() {
			reader("0").apply { readInt(); assert(nextToken).toBe(null) }
		}
	}


	private fun reader(reader: Reader): JsonReader =
		StandardReader(TextInput(reader))


	private fun reader(string: String): JsonReader =
		StandardReader(TextInput(StringReader(string)))


	private object Dummy1
	private object Dummy2
	private object Dummy3
}
