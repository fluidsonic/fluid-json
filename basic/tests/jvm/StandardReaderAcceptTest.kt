package tests.basic

import io.fluidsonic.json.*
import java.io.*
import kotlin.test.*


@OptIn(ExperimentalStdlibApi::class)
class StandardReaderAcceptTest {

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

			val innerMap = (0 .. 100).associateBy { it.toString() }
			val outerMap = (0 .. 100).associate { it.toString() to innerMap }

			expect(value).toBe(outerMap)
		}
	}


	@Test
	fun testReadBoolean() {
		expect(reader("true").readBoolean()).toBe(true)
		expect(reader("false").readBoolean()).toBe(false)
	}


	@Test
	fun testBooleanOrNull() {
		expect(reader("true").readBooleanOrNull()).toBe(true)
		expect(reader("false").readBooleanOrNull()).toBe(false)
		expect(reader("null").readBooleanOrNull()).toBe(null)
	}


	@Test
	fun testByte() {
		expect(reader("0").readByte()).toBe(0.toByte())
		expect(reader("-0").readByte()).toBe(0.toByte())
		expect(reader("100").readByte()).toBe(100.toByte())
		expect(reader("-100").readByte()).toBe((-100).toByte())
		expect(reader("0.000").readByte()).toBe(0.toByte())
		expect(reader("-0.000").readByte()).toBe(0.toByte())
		expect(reader("100.001").readByte()).toBe(100.toByte())
		expect(reader("-100.001").readByte()).toBe((-100).toByte())
		expect(reader("100.999").readByte()).toBe(100.toByte())
		expect(reader("-100.999").readByte()).toBe((-100).toByte())
		expect(reader("1e2").readByte()).toBe(100.toByte())
		expect(reader("-1e2").readByte()).toBe((-100).toByte())
		expect(reader("1.0e2").readByte()).toBe(100.toByte())
		expect(reader("-1.0e2").readByte()).toBe((-100).toByte())
		expect(reader("1.0e+2").readByte()).toBe(100.toByte())
		expect(reader("-1.0e+2").readByte()).toBe((-100).toByte())
		expect(reader("1.0e-2").readByte()).toBe(0.toByte())
		expect(reader("-1.0e-2").readByte()).toBe(0.toByte())
	}

	@Test
	fun testByteOrNull() {
		expect(reader("0").readByteOrNull()).toBe(0.toByte())
		expect(reader("-0").readByteOrNull()).toBe(0.toByte())
		expect(reader("100").readByteOrNull()).toBe(100.toByte())
		expect(reader("-100").readByteOrNull()).toBe((-100).toByte())
		expect(reader("0.000").readByteOrNull()).toBe(0.toByte())
		expect(reader("-0.000").readByteOrNull()).toBe(0.toByte())
		expect(reader("100.001").readByteOrNull()).toBe(100.toByte())
		expect(reader("-100.001").readByteOrNull()).toBe((-100).toByte())
		expect(reader("100.999").readByteOrNull()).toBe(100.toByte())
		expect(reader("-100.999").readByteOrNull()).toBe((-100).toByte())
		expect(reader("1e2").readByteOrNull()).toBe(100.toByte())
		expect(reader("-1e2").readByteOrNull()).toBe((-100).toByte())
		expect(reader("1.0e2").readByteOrNull()).toBe(100.toByte())
		expect(reader("-1.0e2").readByteOrNull()).toBe((-100).toByte())
		expect(reader("1.0e+2").readByteOrNull()).toBe(100.toByte())
		expect(reader("-1.0e+2").readByteOrNull()).toBe((-100).toByte())
		expect(reader("1.0e-2").readByteOrNull()).toBe(0.toByte())
		expect(reader("-1.0e-2").readByteOrNull()).toBe(0.toByte())
		expect(reader("null").readByteOrNull()).toBe(null)
	}


	@Test
	fun testReadChar() {
		expect(reader(""""\u0000"""").readChar()).toBe(0.toChar())
		expect(reader(""""a"""").readChar()).toBe('a')
	}


	@Test
	fun testReadCharOrNull() {
		expect(reader(""""\u0000"""").readCharOrNull()).toBe(0.toChar())
		expect(reader(""""a"""").readCharOrNull()).toBe('a')
		expect(reader("null").readCharOrNull()).toBe(null)
	}


	@Test
	fun testReadDouble() {
		expect(reader("0").readDouble()).toBe(0.0)
		expect(reader("-0").readDouble()).toBe(-0.0)
		expect(reader("100").readDouble()).toBe(100.0)
		expect(reader("-100").readDouble()).toBe(-100.0)
		expect(reader("0.000").readDouble()).toBe(0.0)
		expect(reader("-0.000").readDouble()).toBe(-0.0)
		expect(reader("100.001").readDouble()).toBe(100.001)
		expect(reader("-100.001").readDouble()).toBe(-100.001)
		expect(reader("100.999").readDouble()).toBe(100.999)
		expect(reader("-100.999").readDouble()).toBe(-100.999)
		expect(reader("1e2").readDouble()).toBe(100.0)
		expect(reader("-1e2").readDouble()).toBe(-100.0)
		expect(reader("1.0e2").readDouble()).toBe(100.0)
		expect(reader("-1.0e2").readDouble()).toBe(-100.0)
		expect(reader("1.0e+2").readDouble()).toBe(100.0)
		expect(reader("-1.0e+2").readDouble()).toBe(-100.0)
		expect(reader("1.0e-2").readDouble()).toBe(0.01)
		expect(reader("-1.0e-2").readDouble()).toBe(-0.01)
		expect(reader("9223372036854775808").readDouble()).toBe(9223372036854775808.0)
		expect(reader("-9223372036854775809").readDouble()).toBe(-9223372036854775809.0)
		expect(reader("1000000000000000000000000000000").readDouble()).toBe(1000000000000000000000000000000.0)
		expect(reader("-1000000000000000000000000000000").readDouble()).toBe(-1000000000000000000000000000000.0)
		expect(reader("1e20000").readDouble()).toBe(Double.POSITIVE_INFINITY)
		expect(reader("-1e20000").readDouble()).toBe(Double.NEGATIVE_INFINITY)
	}


	@Test
	fun testReadDoubleOrNull() {
		expect(reader("0").readDoubleOrNull()).toBe(0.0)
		expect(reader("-0").readDoubleOrNull()).toBe(-0.0)
		expect(reader("100").readDoubleOrNull()).toBe(100.0)
		expect(reader("-100").readDoubleOrNull()).toBe(-100.0)
		expect(reader("0.000").readDoubleOrNull()).toBe(0.0)
		expect(reader("-0.000").readDoubleOrNull()).toBe(-0.0)
		expect(reader("100.001").readDoubleOrNull()).toBe(100.001)
		expect(reader("-100.001").readDoubleOrNull()).toBe(-100.001)
		expect(reader("100.999").readDoubleOrNull()).toBe(100.999)
		expect(reader("-100.999").readDoubleOrNull()).toBe(-100.999)
		expect(reader("1e2").readDoubleOrNull()).toBe(100.0)
		expect(reader("-1e2").readDoubleOrNull()).toBe(-100.0)
		expect(reader("1.0e2").readDoubleOrNull()).toBe(100.0)
		expect(reader("-1.0e2").readDoubleOrNull()).toBe(-100.0)
		expect(reader("1.0e+2").readDoubleOrNull()).toBe(100.0)
		expect(reader("-1.0e+2").readDoubleOrNull()).toBe(-100.0)
		expect(reader("1.0e-2").readDoubleOrNull()).toBe(0.01)
		expect(reader("-1.0e-2").readDoubleOrNull()).toBe(-0.01)
		expect(reader("9223372036854775808").readDoubleOrNull()).toBe(9223372036854775808.0)
		expect(reader("-9223372036854775809").readDoubleOrNull()).toBe(-9223372036854775809.0)
		expect(reader("1000000000000000000000000000000").readDoubleOrNull()).toBe(1000000000000000000000000000000.0)
		expect(reader("-1000000000000000000000000000000").readDoubleOrNull()).toBe(-1000000000000000000000000000000.0)
		expect(reader("1e20000").readDoubleOrNull()).toBe(Double.POSITIVE_INFINITY)
		expect(reader("-1e20000").readDoubleOrNull()).toBe(Double.NEGATIVE_INFINITY)
		expect(reader("null").readDoubleOrNull()).toBe(null)
	}


	@Test
	fun testReadFloat() {
		expect(reader("0").readFloat()).toBe(0.0f)
		expect(reader("-0").readFloat()).toBe(-0.0f)
		expect(reader("100").readFloat()).toBe(100.0f)
		expect(reader("-100").readFloat()).toBe(-100.0f)
		expect(reader("0.000").readFloat()).toBe(0.0f)
		expect(reader("-0.000").readFloat()).toBe(-0.0f)
		expect(reader("100.001").readFloat()).toBe(100.001f)
		expect(reader("-100.001").readFloat()).toBe(-100.001f)
		expect(reader("100.999").readFloat()).toBe(100.999f)
		expect(reader("-100.999").readFloat()).toBe(-100.999f)
		expect(reader("1e2").readFloat()).toBe(100.0f)
		expect(reader("-1e2").readFloat()).toBe(-100.0f)
		expect(reader("1.0e2").readFloat()).toBe(100.0f)
		expect(reader("-1.0e2").readFloat()).toBe(-100.0f)
		expect(reader("1.0e+2").readFloat()).toBe(100.0f)
		expect(reader("-1.0e+2").readFloat()).toBe(-100.0f)
		expect(reader("1.0e-2").readFloat()).toBe(0.01f)
		expect(reader("-1.0e-2").readFloat()).toBe(-0.01f)
		expect(reader("9223372036854775808").readFloat()).toBe(9223372036854775808.0f)
		expect(reader("-9223372036854775809").readFloat()).toBe(-9223372036854775809.0f)
		expect(reader("1000000000000000000000000000000").readFloat()).toBe(1000000000000000000000000000000.0f)
		expect(reader("-1000000000000000000000000000000").readFloat()).toBe(-1000000000000000000000000000000.0f)
		expect(reader("1e20000").readFloat()).toBe(Float.POSITIVE_INFINITY)
		expect(reader("-1e20000").readFloat()).toBe(Float.NEGATIVE_INFINITY)
	}


	@Test
	fun testReadFloatOrNull() {
		expect(reader("0").readFloatOrNull()).toBe(0.0f)
		expect(reader("-0").readFloatOrNull()).toBe(-0.0f)
		expect(reader("100").readFloatOrNull()).toBe(100.0f)
		expect(reader("-100").readFloatOrNull()).toBe(-100.0f)
		expect(reader("0.000").readFloatOrNull()).toBe(0.0f)
		expect(reader("-0.000").readFloatOrNull()).toBe(-0.0f)
		expect(reader("100.001").readFloatOrNull()).toBe(100.001f)
		expect(reader("-100.001").readFloatOrNull()).toBe(-100.001f)
		expect(reader("100.999").readFloatOrNull()).toBe(100.999f)
		expect(reader("-100.999").readFloatOrNull()).toBe(-100.999f)
		expect(reader("1e2").readFloatOrNull()).toBe(100.0f)
		expect(reader("-1e2").readFloatOrNull()).toBe(-100.0f)
		expect(reader("1.0e2").readFloatOrNull()).toBe(100.0f)
		expect(reader("-1.0e2").readFloatOrNull()).toBe(-100.0f)
		expect(reader("1.0e+2").readFloatOrNull()).toBe(100.0f)
		expect(reader("-1.0e+2").readFloatOrNull()).toBe(-100.0f)
		expect(reader("1.0e-2").readFloatOrNull()).toBe(0.01f)
		expect(reader("-1.0e-2").readFloatOrNull()).toBe(-0.01f)
		expect(reader("9223372036854775808").readFloatOrNull()).toBe(9223372036854775808.0f)
		expect(reader("-9223372036854775809").readFloatOrNull()).toBe(-9223372036854775809.0f)
		expect(reader("1000000000000000000000000000000").readFloatOrNull()).toBe(1000000000000000000000000000000.0f)
		expect(reader("-1000000000000000000000000000000").readFloatOrNull()).toBe(-1000000000000000000000000000000.0f)
		expect(reader("1e20000").readFloatOrNull()).toBe(Float.POSITIVE_INFINITY)
		expect(reader("-1e20000").readFloatOrNull()).toBe(Float.NEGATIVE_INFINITY)
		expect(reader("null").readFloatOrNull()).toBe(null)
	}


	@Test
	fun testReadFromList() {
		reader("[]").apply {
			expect(readFromList { Dummy1 }).toBe(Dummy1)
		}

		reader("[ \t\n\r]").apply {
			expect(readFromList { Dummy1 }).toBe(Dummy1)
		}

		reader("[1]").apply {
			expect(readFromList {
				skipValue()
				Dummy1
			}).toBe(Dummy1)
		}

		reader("[ true, \"hey\", null ]").apply {
			expect(readFromList {
				skipValue()
				skipValue()
				skipValue()
				Dummy1
			}).toBe(Dummy1)
		}

		reader("[ [], [ 1 ] ]").apply {
			expect(readFromList {
				expect(readFromList { Dummy2 }).toBe(Dummy2)
				expect(readFromList {
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
			val list = buildList {
				readFromListByElement { add(readInt()) }
			}
			expect(list).toBe(listOf(1, 2))
		}
	}


	@Test
	fun testReadFromMapInline() {
		reader("{}").apply {
			expect(readFromMap { Dummy1 }).toBe(Dummy1)
		}
		reader("{ \t\n\r}").apply {
			expect(readFromMap { Dummy1 }).toBe(Dummy1)
		}
		reader("{\"key\":1}").apply {
			expect(readFromMap {
				expect(readString()).toBe("key")
				expect(readInt()).toBe(1)
				Dummy1
			}).toBe(Dummy1)
		}
		reader("{ \"key0\": true, \"key1\" :\"hey\", \"key2\" : null }").apply {
			expect(readFromMap {
				expect(readString()).toBe("key0")
				expect(readBoolean()).toBe(true)
				expect(readString()).toBe("key1")
				expect(readString()).toBe("hey")
				expect(readString()).toBe("key2")
				readNull()
				Dummy1
			}).toBe(Dummy1)
		}
		reader("{ \"key0\": {}, \"key1\": { \"key\": 1 } }").apply {
			expect(readFromMap {
				expect(readString()).toBe("key0")
				expect(readFromMap { Dummy2 }).toBe(Dummy2)
				expect(readString()).toBe("key1")
				expect(readFromMap {
					expect(readString()).toBe("key")
					expect(readInt()).toBe(1)
					Dummy3
				}).toBe(Dummy3)
				Dummy1
			}).toBe(Dummy1)
		}
		reader("{ \" \\\\ \\\" \\/ \\b \\f \\n \\r \\t \\uD83D\\udc36 \": 1 }").apply {
			expect(readFromMap {
				expect(readString()).toBe(" \\ \" / \b \u000C \n \r \t 🐶 ")
				expect(readInt()).toBe(1)
				Dummy1
			}).toBe(Dummy1)
		}
		reader("{ \"0\": 0, \"2\": 2, \"1\": 1, \"3\": 3, \"-1\": -1 }").apply {
			expect(readFromMap {
				expect(readString()).toBe("0")
				expect(readInt()).toBe(0)
				expect(readString()).toBe("2")
				expect(readInt()).toBe(2)
				expect(readString()).toBe("1")
				expect(readInt()).toBe(1)
				expect(readString()).toBe("3")
				expect(readInt()).toBe(3)
				expect(readString()).toBe("-1")
				expect(readInt()).toBe(-1)
				Dummy1
			}).toBe(Dummy1)
		}
	}


	@Test
	fun testReadFromMapByElement() {
		reader("""{ "one": 1, "two": 2 }""").apply {
			val map = buildMap<String, Int> {
				readFromMapByElement { put(readMapKey(), readInt()) }
			}
			expect(map).toBe(mapOf("one" to 1, "two" to 2))
		}
	}


	@Test
	fun testReadFromMapByElementValue() {
		reader("""{ "one": 1, "two": 2 }""").apply {
			val map = buildMap<String, Int> {
				readFromMapByElementValue { key -> put(key, readInt()) }
			}
			expect(map).toBe(mapOf("one" to 1, "two" to 2))
		}
	}


	@Test
	fun testReadInt() {
		expect(reader("0").readInt()).toBe(0)
		expect(reader("-0").readInt()).toBe(0)
		expect(reader("100").readInt()).toBe(100)
		expect(reader("-100").readInt()).toBe(-100)
		expect(reader("0.000").readInt()).toBe(0)
		expect(reader("-0.000").readInt()).toBe(0)
		expect(reader("100.001").readInt()).toBe(100)
		expect(reader("-100.001").readInt()).toBe(-100)
		expect(reader("100.999").readInt()).toBe(100)
		expect(reader("-100.999").readInt()).toBe(-100)
		expect(reader("1e2").readInt()).toBe(100)
		expect(reader("-1e2").readInt()).toBe(-100)
		expect(reader("1.0e+2").readInt()).toBe(100)
		expect(reader("-1.0e+2").readInt()).toBe(-100)
		expect(reader("1.0e2").readInt()).toBe(100)
		expect(reader("-1.0e2").readInt()).toBe(-100)
		expect(reader("1.0e-2").readInt()).toBe(0)
		expect(reader("-1.0e-2").readInt()).toBe(0)
	}


	@Test
	fun testReadIntOrNull() {
		expect(reader("0").readIntOrNull()).toBe(0)
		expect(reader("-0").readIntOrNull()).toBe(0)
		expect(reader("100").readIntOrNull()).toBe(100)
		expect(reader("-100").readIntOrNull()).toBe(-100)
		expect(reader("0.000").readIntOrNull()).toBe(0)
		expect(reader("-0.000").readIntOrNull()).toBe(0)
		expect(reader("100.001").readIntOrNull()).toBe(100)
		expect(reader("-100.001").readIntOrNull()).toBe(-100)
		expect(reader("100.999").readIntOrNull()).toBe(100)
		expect(reader("-100.999").readIntOrNull()).toBe(-100)
		expect(reader("1e2").readIntOrNull()).toBe(100)
		expect(reader("-1e2").readIntOrNull()).toBe(-100)
		expect(reader("1.0e2").readIntOrNull()).toBe(100)
		expect(reader("-1.0e2").readIntOrNull()).toBe(-100)
		expect(reader("1.0e+2").readIntOrNull()).toBe(100)
		expect(reader("-1.0e+2").readIntOrNull()).toBe(-100)
		expect(reader("1.0e-2").readIntOrNull()).toBe(0)
		expect(reader("-1.0e-2").readIntOrNull()).toBe(0)
		expect(reader("null").readIntOrNull()).toBe(null)
	}


	@Test
	fun testReadList() {
		expect(reader("[]").readList()).toBe(emptyList())
		expect(reader("[ \t\n\r]").readList()).toBe(emptyList())
		expect(reader("[1]").readList()).toBe(listOf(1))
		expect(reader("[ true, \"hey\", null ]").readList()).toBe(listOf(true, "hey", null))
		expect(reader("[ [], [ 1 ] ]").readList()).toBe(listOf(emptyList<Any?>(), listOf(1)))
	}


	@Test
	fun testReadListByElement() {
		expect(reader("[ true, \"hey\", null ]").readListByElement { readValueOrNull() })
			.toBe(listOf(true, "hey", null))
	}


	@Test
	fun testReadListEnd() {
		reader("[]").apply {
			readListStart()
			expect(nextToken).toBe(JsonToken.listEnd)
			readListEnd()
			expect(nextToken).toBe(null)
		}
	}


	@Test
	fun testReadListOrNull() {
		expect(reader("[]").readListOrNull()).toBe(emptyList())
		expect(reader("[ \t\n\r]").readListOrNull()).toBe(emptyList())
		expect(reader("[1]").readListOrNull()).toBe(listOf(1))
		expect(reader("[ true, \"hey\", null ]").readListOrNull()).toBe(listOf(true, "hey", null))
		expect(reader("[ [], [ 1 ] ]").readListOrNull()).toBe(listOf(emptyList<Any?>(), listOf(1)))
		expect(reader("null").readListOrNull()).toBe(null)
	}


	@Test
	fun testReadListOrNullByElement() {
		expect(reader("[ true, \"hey\", null ]").readListOrNullByElement { readValueOrNull() })
			.toBe(listOf(true, "hey", null))

		expect(reader("null").readListOrNullByElement { readValueOrNull() })
			.toBe(null)
	}


	@Test
	fun testReadListStart() {
		reader("[]").apply {
			expect(nextToken).toBe(JsonToken.listStart)
			readListStart()
			expect(nextToken).toBe(JsonToken.listEnd)
		}
	}


	@Test
	fun testReadLong() {
		expect(reader("0").readLong()).toBe(0L)
		expect(reader("-0").readLong()).toBe(0L)
		expect(reader("100").readLong()).toBe(100L)
		expect(reader("-100").readLong()).toBe(-100L)
		expect(reader("0.000").readLong()).toBe(0L)
		expect(reader("-0.000").readLong()).toBe(0L)
		expect(reader("100.001").readLong()).toBe(100L)
		expect(reader("-100.001").readLong()).toBe(-100L)
		expect(reader("100.999").readLong()).toBe(100L)
		expect(reader("-100.999").readLong()).toBe(-100L)
		expect(reader("1e2").readLong()).toBe(100L)
		expect(reader("-1e2").readLong()).toBe(-100L)
		expect(reader("1.0e2").readLong()).toBe(100L)
		expect(reader("-1.0e2").readLong()).toBe(-100L)
		expect(reader("1.0e+2").readLong()).toBe(100L)
		expect(reader("-1.0e+2").readLong()).toBe(-100L)
		expect(reader("1.0e-2").readLong()).toBe(0L)
		expect(reader("-1.0e-2").readLong()).toBe(0L)
	}


	@Test
	fun testReadLongOrNull() {
		expect(reader("0").readLongOrNull()).toBe(0L)
		expect(reader("-0").readLongOrNull()).toBe(0L)
		expect(reader("100").readLongOrNull()).toBe(100L)
		expect(reader("-100").readLongOrNull()).toBe(-100L)
		expect(reader("0.000").readLongOrNull()).toBe(0L)
		expect(reader("-0.000").readLongOrNull()).toBe(0L)
		expect(reader("100.001").readLongOrNull()).toBe(100L)
		expect(reader("-100.001").readLongOrNull()).toBe(-100L)
		expect(reader("100.999").readLongOrNull()).toBe(100L)
		expect(reader("-100.999").readLongOrNull()).toBe(-100L)
		expect(reader("1e2").readLongOrNull()).toBe(100L)
		expect(reader("-1e2").readLongOrNull()).toBe(-100L)
		expect(reader("1.0e2").readLongOrNull()).toBe(100L)
		expect(reader("-1.0e2").readLongOrNull()).toBe(-100L)
		expect(reader("1.0e+2").readLongOrNull()).toBe(100L)
		expect(reader("-1.0e+2").readLongOrNull()).toBe(-100L)
		expect(reader("1.0e-2").readLongOrNull()).toBe(0L)
		expect(reader("-1.0e-2").readLongOrNull()).toBe(0L)
		expect(reader("null").readLongOrNull()).toBe(null)
	}


	@Test
	fun testReadMap() {
		expect(reader("{}").readMap()).toBe(emptyMap())
		expect(reader("{ \t\n\r}").readMap()).toBe(emptyMap())
		expect(reader("{\"key\":1}").readMap()).toBe(mapOf("key" to 1))
		expect(reader("{ \"key0\": true, \"key1\" :\"hey\", \"key2\" : null }").readMap()).toBe(mapOf(
			"key0" to true,
			"key1" to "hey",
			"key2" to null
		))
		expect(reader("{ \"key0\": {}, \"key1\": { \"key\": 1 } }").readMap()).toBe(mapOf(
			"key0" to emptyMap<String, Any>(),
			"key1" to mapOf("key" to 1)
		))
		expect(reader("{ \" \\\\ \\\" \\/ \\b \\f \\n \\r \\t \\uD83D\\udc36 \": 1 }").readMap()).toBe(mapOf(
			" \\ \" / \b \u000C \n \r \t 🐶 " to 1
		))
		expect(reader("{ \"0\": 0, \"2\": 2, \"1\": 1, \"3\": 3, \"-1\": -1 }").readMap().toList()).toBe(listOf(
			"0" to 0,
			"2" to 2,
			"1" to 1,
			"3" to 3,
			"-1" to -1
		))
	}


	@Test
	fun testReadMapByElement() {
		expect(
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
		expect(
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
			expect(nextToken).toBe(JsonToken.mapEnd)
			readMapEnd()
			expect(nextToken).toBe(null)
		}
	}


	@Test
	fun testReadMapKey() {
		reader("{\"\"").apply {
			readMapStart()
			expect(readMapKey()).toBe("")
		}
		reader("{\"simple\"").apply {
			readMapStart()
			expect(readMapKey()).toBe("simple")
		}
		reader("{\" a bit longer \"").apply {
			readMapStart()
			expect(readMapKey()).toBe(" a bit longer ")
		}
		reader("{\"a dog: 🐶\"").apply {
			readMapStart()
			expect(readMapKey()).toBe("a dog: 🐶")
		}
		reader("{\" \\\\ \\\" \\/ \\b \\f \\n \\r \\t \\uD83D\\udc36 \"").apply {
			readMapStart()
			expect(readMapKey()).toBe(" \\ \" / \b \u000C \n \r \t 🐶 ")
		}
	}


	@Test
	fun testReadMapOrNull() {
		expect(reader("{}").readMapOrNull()).toBe(emptyMap())
		expect(reader("{ \t\n\r}").readMapOrNull()).toBe(emptyMap())
		expect(reader("{\"key\":1}").readMapOrNull()).toBe(mapOf("key" to 1))
		expect(reader("{ \"key0\": true, \"key1\" :\"hey\", \"key2\" : null }").readMapOrNull()).toBe(mapOf(
			"key0" to true,
			"key1" to "hey",
			"key2" to null
		))
		expect(reader("{ \"key0\": {}, \"key1\": { \"key\": 1 } }").readMapOrNull()).toBe(mapOf(
			"key0" to emptyMap<String, Any>(),
			"key1" to mapOf("key" to 1)
		))
		expect(reader("{ \" \\\\ \\\" \\/ \\b \\f \\n \\r \\t \\uD83D\\udc36 \": 1 }").readMapOrNull()).toBe(mapOf(
			" \\ \" / \b \u000C \n \r \t 🐶 " to 1
		))
		expect(reader("{ \"0\": 0, \"2\": 2, \"1\": 1, \"3\": 3, \"-1\": -1 }").readMapOrNull()?.toList()).toBe(listOf(
			"0" to 0,
			"2" to 2,
			"1" to 1,
			"3" to 3,
			"-1" to -1
		))
		expect(reader("null").readMapOrNull()).toBe(null)
	}


	@Test
	fun testReadMapOrNullByElement() {
		expect(
			reader("{ \"key0\": true, \"key1\" :\"hey\", \"key2\" : null }")
				.readMapOrNullByElement { readMapKey() to readValueOrNull() }
		)
			.toBe(mapOf(
				"key0" to true,
				"key1" to "hey",
				"key2" to null
			))

		expect(
			reader("null")
				.readMapOrNullByElement { readMapKey() to readValueOrNull() }
		)
			.toBe(null)
	}


	@Test
	fun testReadMapOrNullByElementValue() {
		expect(
			reader("{ \"key0\": true, \"key1\" :\"hey\", \"key2\" : null }")
				.readMapOrNullByElementValue { readValueOrNull() }
		)
			.toBe(mapOf(
				"key0" to true,
				"key1" to "hey",
				"key2" to null
			))

		expect(
			reader("null")
				.readMapOrNullByElementValue { readValueOrNull() }
		)
			.toBe(null)
	}


	@Test
	fun testReadMapStart() {
		reader("{}").apply {
			expect(nextToken).toBe(JsonToken.mapStart)
			readMapStart()
			expect(nextToken).toBe(JsonToken.mapEnd)
		}
	}


	@Test
	fun testReadNull() {
		expect((reader("null").readNull() as Any?)).toBe(null)
	}


	@Test
	fun testReadNumber() {
		expect(reader("0").readNumber()).toBe(0)
		expect(reader("-0").readNumber()).toBe(0)
		expect(reader("100").readNumber()).toBe(100)
		expect(reader("-100").readNumber()).toBe(-100)
		expect(reader("0.000").readNumber()).toBe(0.0)
		expect(reader("-0.000").readNumber()).toBe(-0.0)
		expect(reader("100.001").readNumber()).toBe(100.001)
		expect(reader("-100.001").readNumber()).toBe(-100.001)
		expect(reader("100.999").readNumber()).toBe(100.999)
		expect(reader("-100.999").readNumber()).toBe(-100.999)
		expect(reader("2147483647").readNumber()).toBe(2147483647)
		expect(reader("-2147483648").readNumber()).toBe(-2147483648)
		expect(reader("2147483648").readNumber()).toBe(2147483648L)
		expect(reader("-2147483649").readNumber()).toBe(-2147483649L)
		expect(reader("2147483647").readNumber()).toBe(2147483647)
		expect(reader("-2147483648").readNumber()).toBe(-2147483648)
		expect(reader("9223372036854775807").readNumber()).toBe(9223372036854775807L)
		expect(reader("-9223372036854775808").readNumber()).toBe(-9223372036854775807L - 1) // https://youtrack.jetbrains.com/issue/KT-17172
		expect(reader("9223372036854775808").readNumber()).toBe(9223372036854775808.0)
		expect(reader("-9223372036854775809").readNumber()).toBe(-9223372036854775809.0)
		expect(reader("1e2").readNumber()).toBe(100.0)
		expect(reader("-1e2").readNumber()).toBe(-100.0)
		expect(reader("1.0e2").readNumber()).toBe(100.0)
		expect(reader("-1.0e2").readNumber()).toBe(-100.0)
		expect(reader("1.0e-2").readNumber()).toBe(0.01)
		expect(reader("-1.0e-2").readNumber()).toBe(-0.01)
		expect(reader("1000000000000000000000000000000").readNumber()).toBe(1000000000000000000000000000000.0)
		expect(reader("-1000000000000000000000000000000").readNumber()).toBe(-1000000000000000000000000000000.0)
		expect(reader("1e20000").readNumber()).toBe(Double.POSITIVE_INFINITY)
		expect(reader("-1e20000").readNumber()).toBe(Double.NEGATIVE_INFINITY)
	}


	@Test
	fun testReadNumberOrNull() {
		expect(reader("0").readNumberOrNull()).toBe(0)
		expect(reader("-0").readNumberOrNull()).toBe(0)
		expect(reader("100").readNumberOrNull()).toBe(100)
		expect(reader("-100").readNumberOrNull()).toBe(-100)
		expect(reader("0.000").readNumberOrNull()).toBe(0.0)
		expect(reader("-0.000").readNumberOrNull()).toBe(-0.0)
		expect(reader("100.001").readNumberOrNull()).toBe(100.001)
		expect(reader("-100.001").readNumberOrNull()).toBe(-100.001)
		expect(reader("100.999").readNumberOrNull()).toBe(100.999)
		expect(reader("-100.999").readNumberOrNull()).toBe(-100.999)
		expect(reader("2147483647").readNumberOrNull()).toBe(2147483647)
		expect(reader("-2147483648").readNumberOrNull()).toBe(-2147483648)
		expect(reader("2147483648").readNumberOrNull()).toBe(2147483648L)
		expect(reader("-2147483649").readNumberOrNull()).toBe(-2147483649L)
		expect(reader("2147483647").readNumberOrNull()).toBe(2147483647)
		expect(reader("-2147483648").readNumberOrNull()).toBe(-2147483648)
		expect(reader("9223372036854775807").readNumberOrNull()).toBe(9223372036854775807L)
		expect(reader("-9223372036854775808").readNumberOrNull()).toBe(-9223372036854775807L - 1) // https://youtrack.jetbrains.com/issue/KT-17172
		expect(reader("9223372036854775808").readNumberOrNull()).toBe(9223372036854775808.0)
		expect(reader("-9223372036854775809").readNumberOrNull()).toBe(-9223372036854775809.0)
		expect(reader("1e2").readNumberOrNull()).toBe(100.0)
		expect(reader("-1e2").readNumberOrNull()).toBe(-100.0)
		expect(reader("1.0e2").readNumberOrNull()).toBe(100.0)
		expect(reader("-1.0e2").readNumberOrNull()).toBe(-100.0)
		expect(reader("1.0e-2").readNumberOrNull()).toBe(0.01)
		expect(reader("-1.0e-2").readNumberOrNull()).toBe(-0.01)
		expect(reader("1000000000000000000000000000000").readNumberOrNull()).toBe(1000000000000000000000000000000.0)
		expect(reader("-1000000000000000000000000000000").readNumberOrNull()).toBe(-1000000000000000000000000000000.0)
		expect(reader("1e20000").readNumberOrNull()).toBe(Double.POSITIVE_INFINITY)
		expect(reader("-1e20000").readNumberOrNull()).toBe(Double.NEGATIVE_INFINITY)
		expect(reader("null").readNumberOrNull()).toBe(null)
	}


	@Test
	fun testReadShort() {
		expect(reader("0").readShort()).toBe(0.toShort())
		expect(reader("-0").readShort()).toBe(0.toShort())
		expect(reader("100").readShort()).toBe(100.toShort())
		expect(reader("-100").readShort()).toBe((-100).toShort())
		expect(reader("0.000").readShort()).toBe(0.toShort())
		expect(reader("-0.000").readShort()).toBe(0.toShort())
		expect(reader("100.001").readShort()).toBe(100.toShort())
		expect(reader("-100.001").readShort()).toBe((-100).toShort())
		expect(reader("100.999").readShort()).toBe(100.toShort())
		expect(reader("-100.999").readShort()).toBe((-100).toShort())
		expect(reader("1e2").readShort()).toBe(100.toShort())
		expect(reader("-1e2").readShort()).toBe((-100).toShort())
		expect(reader("1.0e2").readShort()).toBe(100.toShort())
		expect(reader("-1.0e2").readShort()).toBe((-100).toShort())
		expect(reader("1.0e+2").readShort()).toBe(100.toShort())
		expect(reader("-1.0e+2").readShort()).toBe((-100).toShort())
		expect(reader("1.0e-2").readShort()).toBe(0.toShort())
		expect(reader("-1.0e-2").readShort()).toBe(0.toShort())
	}


	@Test
	fun testReadShortOrNull() {
		expect(reader("0").readShortOrNull()).toBe(0.toShort())
		expect(reader("-0").readShortOrNull()).toBe(0.toShort())
		expect(reader("100").readShortOrNull()).toBe(100.toShort())
		expect(reader("-100").readShortOrNull()).toBe((-100).toShort())
		expect(reader("0.000").readShortOrNull()).toBe(0.toShort())
		expect(reader("-0.000").readShortOrNull()).toBe(0.toShort())
		expect(reader("100.001").readShortOrNull()).toBe(100.toShort())
		expect(reader("-100.001").readShortOrNull()).toBe((-100).toShort())
		expect(reader("100.999").readShortOrNull()).toBe(100.toShort())
		expect(reader("-100.999").readShortOrNull()).toBe((-100).toShort())
		expect(reader("1e2").readShortOrNull()).toBe(100.toShort())
		expect(reader("-1e2").readShortOrNull()).toBe((-100).toShort())
		expect(reader("1.0e2").readShortOrNull()).toBe(100.toShort())
		expect(reader("-1.0e2").readShortOrNull()).toBe((-100).toShort())
		expect(reader("1.0e+2").readShortOrNull()).toBe(100.toShort())
		expect(reader("-1.0e+2").readShortOrNull()).toBe((-100).toShort())
		expect(reader("1.0e-2").readShortOrNull()).toBe(0.toShort())
		expect(reader("-1.0e-2").readShortOrNull()).toBe(0.toShort())
		expect(reader("null").readShortOrNull()).toBe(null)
	}


	@Test
	fun testReadString() {
		expect(reader("\"\"").readString()).toBe("")
		expect(reader("\"\\u0022\"").readString()).toBe("\"")
		expect(reader("\"simple\"").readString()).toBe("simple")
		expect(reader("\" a bit longer \"").readString()).toBe(" a bit longer ")
		expect(reader("\"a dog: 🐶\"").readString()).toBe("a dog: 🐶")
		expect(reader("\" \\\\ \\\" \\/ \\b \\f \\n \\r \\t \\uD83D\\udc36 \"").readString()).toBe(" \\ \" / \b \u000C \n \r \t 🐶 ")
		reader("{\"key\"").apply { readMapStart();expect(readString()).toBe("key") }
	}


	@Test
	fun testReadStringOrNull() {
		expect(reader("\"\"").readStringOrNull()).toBe("")
		expect(reader("\"\\u0022\"").readStringOrNull()).toBe("\"")
		expect(reader("\"simple\"").readStringOrNull()).toBe("simple")
		expect(reader("\" a bit longer \"").readStringOrNull()).toBe(" a bit longer ")
		expect(reader("\"a dog: 🐶\"").readStringOrNull()).toBe("a dog: 🐶")
		expect(reader("\" \\\\ \\\" \\/ \\b \\f \\n \\r \\t \\uD83D\\udc36 \"").readStringOrNull()).toBe(" \\ \" / \b \u000C \n \r \t 🐶 ")
		expect(reader("null").readStringOrNull()).toBe(null)
	}


	@Test
	fun testReadValue() {
		reader("[true, 0, [], {}, \"\", {\"\":true}]").apply {
			readFromList {
				expect(readValue()).toBe(true)
				expect(readValue()).toBe(0)
				expect(readValue()).toBe(emptyList<Any?>())
				expect(readValue()).toBe(emptyMap<String, Any?>())
				expect(readValue()).toBe("")
				readFromMap {
					expect(readValue()).toBe("")
					expect(readValue()).toBe(true)
				}
			}
		}
	}


	@Test
	fun testReadValueOrNull() {
		reader("[null, true, 0, [], {}, \"\", {\"\":true}]").apply {
			readFromList {
				expect(readValueOrNull()).toBe(null)
				expect(readValueOrNull()).toBe(true)
				expect(readValueOrNull()).toBe(0)
				expect(readValueOrNull()).toBe(emptyList<Any?>())
				expect(readValueOrNull()).toBe(emptyMap<String, Any?>())
				expect(readValueOrNull()).toBe("")
				readFromMap {
					expect(readValueOrNull()).toBe("")
					expect(readValueOrNull()).toBe(true)
				}
			}
		}
	}

	@Test
	fun testSkipValue() {
		reader("[null, 0, [], {}, \"\", {\"\":true}]").apply {
			readFromList {
				expect(nextToken).toBe(JsonToken.nullValue)
				skipValue()
				expect(nextToken).toBe(JsonToken.numberValue)
				skipValue()
				expect(nextToken).toBe(JsonToken.listStart)
				skipValue()
				expect(nextToken).toBe(JsonToken.mapStart)
				skipValue()
				expect(nextToken).toBe(JsonToken.stringValue)
				skipValue()
				expect(nextToken).toBe(JsonToken.mapStart)
				readFromMap {
					expect(nextToken).toBe(JsonToken.mapKey)
					skipValue()
					expect(nextToken).toBe(JsonToken.booleanValue)
					skipValue()
					expect(nextToken).toBe(JsonToken.mapEnd)
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


	@Test
	fun testMatchesFullToken() {
		expect(reader("null").nextToken).toBe(JsonToken.nullValue)
		expect(reader("true").nextToken).toBe(JsonToken.booleanValue)
		expect(reader("false").nextToken).toBe(JsonToken.booleanValue)
		expect(reader("0").nextToken).toBe(JsonToken.numberValue)
		expect(reader("1").nextToken).toBe(JsonToken.numberValue)
		expect(reader("-1.0").nextToken).toBe(JsonToken.numberValue)
		expect(reader("\"\"").nextToken).toBe(JsonToken.stringValue)
		reader("[]").apply {
			expect(nextToken).toBe(JsonToken.listStart)
			readListStart()
			expect(nextToken).toBe(JsonToken.listEnd)
		}
		reader("{}").apply {
			expect(nextToken).toBe(JsonToken.mapStart)
			readMapStart()
			expect(nextToken).toBe(JsonToken.mapEnd)
		}
	}


	@Test
	fun testConsumesMinimalInput() {
		expect(reader("n").nextToken).toBe(JsonToken.nullValue)
		expect(reader("t").nextToken).toBe(JsonToken.booleanValue)
		expect(reader("f").nextToken).toBe(JsonToken.booleanValue)
		expect(reader("0").nextToken).toBe(JsonToken.numberValue)
		expect(reader("1").nextToken).toBe(JsonToken.numberValue)
		expect(reader("-").nextToken).toBe(JsonToken.numberValue)
		expect(reader("[").nextToken).toBe(JsonToken.listStart)
		expect(reader("\"").nextToken).toBe(JsonToken.stringValue)
		expect(reader("{").nextToken).toBe(JsonToken.mapStart)
	}


	@Test
	fun testIdempotence() {
		reader("n").apply { nextToken;expect(nextToken).toBe(JsonToken.nullValue) }
		reader("t").apply { nextToken;expect(nextToken).toBe(JsonToken.booleanValue) }
		reader("f").apply { nextToken;expect(nextToken).toBe(JsonToken.booleanValue) }
		reader("0").apply { nextToken;expect(nextToken).toBe(JsonToken.numberValue) }
		reader("1").apply { nextToken;expect(nextToken).toBe(JsonToken.numberValue) }
		reader("-").apply { nextToken;expect(nextToken).toBe(JsonToken.numberValue) }
		reader("[").apply { nextToken;expect(nextToken).toBe(JsonToken.listStart) }
		reader("\"").apply { nextToken;expect(nextToken).toBe(JsonToken.stringValue) }
		reader("{").apply { nextToken;expect(nextToken).toBe(JsonToken.mapStart) }
	}


	@Test
	fun testCompletesWithNull() {
		reader("0").apply { readInt(); expect(nextToken).toBe(null) }
	}


	private fun reader(reader: Reader): JsonReader =
		StandardReader(TextInput(reader))


	private fun reader(string: String): JsonReader =
		StandardReader(TextInput(StringReader(string)))


	private object Dummy1
	private object Dummy2
	private object Dummy3
}
