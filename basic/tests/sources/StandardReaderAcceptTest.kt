package tests.basic

import ch.tutteli.atrium.api.cc.en_GB.notToBeNullBut
import ch.tutteli.atrium.api.cc.en_GB.toBe
import ch.tutteli.atrium.verbs.assert
import com.github.fluidsonic.fluid.json.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.io.Reader
import java.io.StringReader


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
		assert(reader("true").readBooleanOrNull()).notToBeNullBut(true)
		assert(reader("false").readBooleanOrNull()).notToBeNullBut(false)
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
		assert(reader("0").readByteOrNull()).notToBeNullBut(0.toByte())
		assert(reader("-0").readByteOrNull()).notToBeNullBut(0.toByte())
		assert(reader("100").readByteOrNull()).notToBeNullBut(100.toByte())
		assert(reader("-100").readByteOrNull()).notToBeNullBut((-100).toByte())
		assert(reader("0.000").readByteOrNull()).notToBeNullBut(0.toByte())
		assert(reader("-0.000").readByteOrNull()).notToBeNullBut(0.toByte())
		assert(reader("100.001").readByteOrNull()).notToBeNullBut(100.toByte())
		assert(reader("-100.001").readByteOrNull()).notToBeNullBut((-100).toByte())
		assert(reader("100.999").readByteOrNull()).notToBeNullBut(100.toByte())
		assert(reader("-100.999").readByteOrNull()).notToBeNullBut((-100).toByte())
		assert(reader("1e2").readByteOrNull()).notToBeNullBut(100.toByte())
		assert(reader("-1e2").readByteOrNull()).notToBeNullBut((-100).toByte())
		assert(reader("1.0e2").readByteOrNull()).notToBeNullBut(100.toByte())
		assert(reader("-1.0e2").readByteOrNull()).notToBeNullBut((-100).toByte())
		assert(reader("1.0e+2").readByteOrNull()).notToBeNullBut(100.toByte())
		assert(reader("-1.0e+2").readByteOrNull()).notToBeNullBut((-100).toByte())
		assert(reader("1.0e-2").readByteOrNull()).notToBeNullBut(0.toByte())
		assert(reader("-1.0e-2").readByteOrNull()).notToBeNullBut(0.toByte())
		assert(reader("null").readByteOrNull()).toBe(null)
	}


	@Test
	fun testReadChar() {
		assert(reader(""""\u0000"""").readChar()).toBe(0.toChar())
		assert(reader(""""a"""").readChar()).toBe('a')
	}


	@Test
	fun testReadCharOrNull() {
		assert(reader(""""\u0000"""").readCharOrNull()).notToBeNullBut(0.toChar())
		assert(reader(""""a"""").readCharOrNull()).notToBeNullBut('a')
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
		assert(reader("0").readDoubleOrNull()).notToBeNullBut(0.0)
		assert(reader("-0").readDoubleOrNull()).notToBeNullBut(-0.0)
		assert(reader("100").readDoubleOrNull()).notToBeNullBut(100.0)
		assert(reader("-100").readDoubleOrNull()).notToBeNullBut(-100.0)
		assert(reader("0.000").readDoubleOrNull()).notToBeNullBut(0.0)
		assert(reader("-0.000").readDoubleOrNull()).notToBeNullBut(-0.0)
		assert(reader("100.001").readDoubleOrNull()).notToBeNullBut(100.001)
		assert(reader("-100.001").readDoubleOrNull()).notToBeNullBut(-100.001)
		assert(reader("100.999").readDoubleOrNull()).notToBeNullBut(100.999)
		assert(reader("-100.999").readDoubleOrNull()).notToBeNullBut(-100.999)
		assert(reader("1e2").readDoubleOrNull()).notToBeNullBut(100.0)
		assert(reader("-1e2").readDoubleOrNull()).notToBeNullBut(-100.0)
		assert(reader("1.0e2").readDoubleOrNull()).notToBeNullBut(100.0)
		assert(reader("-1.0e2").readDoubleOrNull()).notToBeNullBut(-100.0)
		assert(reader("1.0e+2").readDoubleOrNull()).notToBeNullBut(100.0)
		assert(reader("-1.0e+2").readDoubleOrNull()).notToBeNullBut(-100.0)
		assert(reader("1.0e-2").readDoubleOrNull()).notToBeNullBut(0.01)
		assert(reader("-1.0e-2").readDoubleOrNull()).notToBeNullBut(-0.01)
		assert(reader("9223372036854775808").readDoubleOrNull()).notToBeNullBut(9223372036854775808.0)
		assert(reader("-9223372036854775809").readDoubleOrNull()).notToBeNullBut(-9223372036854775809.0)
		assert(reader("1000000000000000000000000000000").readDoubleOrNull()).notToBeNullBut(1000000000000000000000000000000.0)
		assert(reader("-1000000000000000000000000000000").readDoubleOrNull()).notToBeNullBut(-1000000000000000000000000000000.0)
		assert(reader("1e20000").readDoubleOrNull()).notToBeNullBut(Double.POSITIVE_INFINITY)
		assert(reader("-1e20000").readDoubleOrNull()).notToBeNullBut(Double.NEGATIVE_INFINITY)
		assert(reader("null").readDoubleOrNull()).toBe(null)
	}


	@Test
	fun testReadElementsFromMap() {
		val map = mutableMapOf<String, Any?>()

		reader("{ \"key0\": true, \"key1\" :\"hey\", \"key2\" : null }").readElementsFromMap { key ->
			map[key] = readValueOrNull()
		}

		assert(map).toBe(mapOf(
			"key0" to true,
			"key1" to "hey",
			"key2" to null
		))
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
		assert(reader("0").readFloatOrNull()).notToBeNullBut(0.0f)
		assert(reader("-0").readFloatOrNull()).notToBeNullBut(-0.0f)
		assert(reader("100").readFloatOrNull()).notToBeNullBut(100.0f)
		assert(reader("-100").readFloatOrNull()).notToBeNullBut(-100.0f)
		assert(reader("0.000").readFloatOrNull()).notToBeNullBut(0.0f)
		assert(reader("-0.000").readFloatOrNull()).notToBeNullBut(-0.0f)
		assert(reader("100.001").readFloatOrNull()).notToBeNullBut(100.001f)
		assert(reader("-100.001").readFloatOrNull()).notToBeNullBut(-100.001f)
		assert(reader("100.999").readFloatOrNull()).notToBeNullBut(100.999f)
		assert(reader("-100.999").readFloatOrNull()).notToBeNullBut(-100.999f)
		assert(reader("1e2").readFloatOrNull()).notToBeNullBut(100.0f)
		assert(reader("-1e2").readFloatOrNull()).notToBeNullBut(-100.0f)
		assert(reader("1.0e2").readFloatOrNull()).notToBeNullBut(100.0f)
		assert(reader("-1.0e2").readFloatOrNull()).notToBeNullBut(-100.0f)
		assert(reader("1.0e+2").readFloatOrNull()).notToBeNullBut(100.0f)
		assert(reader("-1.0e+2").readFloatOrNull()).notToBeNullBut(-100.0f)
		assert(reader("1.0e-2").readFloatOrNull()).notToBeNullBut(0.01f)
		assert(reader("-1.0e-2").readFloatOrNull()).notToBeNullBut(-0.01f)
		assert(reader("9223372036854775808").readFloatOrNull()).notToBeNullBut(9223372036854775808.0f)
		assert(reader("-9223372036854775809").readFloatOrNull()).notToBeNullBut(-9223372036854775809.0f)
		assert(reader("1000000000000000000000000000000").readFloatOrNull()).notToBeNullBut(1000000000000000000000000000000.0f)
		assert(reader("-1000000000000000000000000000000").readFloatOrNull()).notToBeNullBut(-1000000000000000000000000000000.0f)
		assert(reader("1e20000").readFloatOrNull()).notToBeNullBut(Float.POSITIVE_INFINITY)
		assert(reader("-1e20000").readFloatOrNull()).notToBeNullBut(Float.NEGATIVE_INFINITY)
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
		assert(reader("0").readIntOrNull()).notToBeNullBut(0)
		assert(reader("-0").readIntOrNull()).notToBeNullBut(0)
		assert(reader("100").readIntOrNull()).notToBeNullBut(100)
		assert(reader("-100").readIntOrNull()).notToBeNullBut(-100)
		assert(reader("0.000").readIntOrNull()).notToBeNullBut(0)
		assert(reader("-0.000").readIntOrNull()).notToBeNullBut(0)
		assert(reader("100.001").readIntOrNull()).notToBeNullBut(100)
		assert(reader("-100.001").readIntOrNull()).notToBeNullBut(-100)
		assert(reader("100.999").readIntOrNull()).notToBeNullBut(100)
		assert(reader("-100.999").readIntOrNull()).notToBeNullBut(-100)
		assert(reader("1e2").readIntOrNull()).notToBeNullBut(100)
		assert(reader("-1e2").readIntOrNull()).notToBeNullBut(-100)
		assert(reader("1.0e2").readIntOrNull()).notToBeNullBut(100)
		assert(reader("-1.0e2").readIntOrNull()).notToBeNullBut(-100)
		assert(reader("1.0e+2").readIntOrNull()).notToBeNullBut(100)
		assert(reader("-1.0e+2").readIntOrNull()).notToBeNullBut(-100)
		assert(reader("1.0e-2").readIntOrNull()).notToBeNullBut(0)
		assert(reader("-1.0e-2").readIntOrNull()).notToBeNullBut(0)
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
			assert(nextToken).notToBeNullBut(JSONToken.listEnd)
			readListEnd()
			assert(nextToken).toBe(null)
		}
	}


	@Test
	fun testReadListOrNull() {
		assert(reader("[]").readListOrNull()).notToBeNullBut(emptyList())
		assert(reader("[ \t\n\r]").readListOrNull()).notToBeNullBut(emptyList())
		assert(reader("[1]").readListOrNull()).notToBeNullBut(listOf(1))
		assert(reader("[ true, \"hey\", null ]").readListOrNull()).notToBeNullBut(listOf(true, "hey", null))
		assert(reader("[ [], [ 1 ] ]").readListOrNull()).notToBeNullBut(listOf(emptyList<Any?>(), listOf(1)))
		assert(reader("null").readListOrNull()).toBe(null)
	}


	@Test
	fun testReadListOrNullByElement() {
		assert(reader("[ true, \"hey\", null ]").readListOrNullByElement { readValueOrNull() })
			.notToBeNullBut(listOf(true, "hey", null))

		assert(reader("null").readListOrNullByElement { readValueOrNull() })
			.toBe(null)
	}


	@Test
	fun testReadListStart() {
		reader("[]").apply {
			assert(nextToken).notToBeNullBut(JSONToken.listStart)
			readListStart()
			assert(nextToken).notToBeNullBut(JSONToken.listEnd)
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
		assert(reader("0").readLongOrNull()).notToBeNullBut(0L)
		assert(reader("-0").readLongOrNull()).notToBeNullBut(0L)
		assert(reader("100").readLongOrNull()).notToBeNullBut(100L)
		assert(reader("-100").readLongOrNull()).notToBeNullBut(-100L)
		assert(reader("0.000").readLongOrNull()).notToBeNullBut(0L)
		assert(reader("-0.000").readLongOrNull()).notToBeNullBut(0L)
		assert(reader("100.001").readLongOrNull()).notToBeNullBut(100L)
		assert(reader("-100.001").readLongOrNull()).notToBeNullBut(-100L)
		assert(reader("100.999").readLongOrNull()).notToBeNullBut(100L)
		assert(reader("-100.999").readLongOrNull()).notToBeNullBut(-100L)
		assert(reader("1e2").readLongOrNull()).notToBeNullBut(100L)
		assert(reader("-1e2").readLongOrNull()).notToBeNullBut(-100L)
		assert(reader("1.0e2").readLongOrNull()).notToBeNullBut(100L)
		assert(reader("-1.0e2").readLongOrNull()).notToBeNullBut(-100L)
		assert(reader("1.0e+2").readLongOrNull()).notToBeNullBut(100L)
		assert(reader("-1.0e+2").readLongOrNull()).notToBeNullBut(-100L)
		assert(reader("1.0e-2").readLongOrNull()).notToBeNullBut(0L)
		assert(reader("-1.0e-2").readLongOrNull()).notToBeNullBut(0L)
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
			assert(nextToken).notToBeNullBut(JSONToken.mapEnd)
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
		assert(reader("{}").readMapOrNull()).notToBeNullBut(emptyMap())
		assert(reader("{ \t\n\r}").readMapOrNull()).notToBeNullBut(emptyMap())
		assert(reader("{\"key\":1}").readMapOrNull()).notToBeNullBut(mapOf("key" to 1))
		assert(reader("{ \"key0\": true, \"key1\" :\"hey\", \"key2\" : null }").readMapOrNull()).notToBeNullBut(mapOf(
			"key0" to true,
			"key1" to "hey",
			"key2" to null
		))
		assert(reader("{ \"key0\": {}, \"key1\": { \"key\": 1 } }").readMapOrNull()).notToBeNullBut(mapOf(
			"key0" to emptyMap<String, Any>(),
			"key1" to mapOf("key" to 1)
		))
		assert(reader("{ \" \\\\ \\\" \\/ \\b \\f \\n \\r \\t \\uD83D\\udc36 \": 1 }").readMapOrNull()).notToBeNullBut(mapOf(
			" \\ \" / \b \u000C \n \r \t 🐶 " to 1
		))
		assert(reader("{ \"0\": 0, \"2\": 2, \"1\": 1, \"3\": 3, \"-1\": -1 }").readMapOrNull()?.toList()).notToBeNullBut(listOf(
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
			.notToBeNullBut(mapOf(
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
			.notToBeNullBut(mapOf(
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
			assert(nextToken).notToBeNullBut(JSONToken.mapStart)
			readMapStart()
			assert(nextToken).notToBeNullBut(JSONToken.mapEnd)
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
		assert(reader("0").readNumberOrNull()).notToBeNullBut(0)
		assert(reader("-0").readNumberOrNull()).notToBeNullBut(0)
		assert(reader("100").readNumberOrNull()).notToBeNullBut(100)
		assert(reader("-100").readNumberOrNull()).notToBeNullBut(-100)
		assert(reader("0.000").readNumberOrNull()).notToBeNullBut(0.0)
		assert(reader("-0.000").readNumberOrNull()).notToBeNullBut(-0.0)
		assert(reader("100.001").readNumberOrNull()).notToBeNullBut(100.001)
		assert(reader("-100.001").readNumberOrNull()).notToBeNullBut(-100.001)
		assert(reader("100.999").readNumberOrNull()).notToBeNullBut(100.999)
		assert(reader("-100.999").readNumberOrNull()).notToBeNullBut(-100.999)
		assert(reader("2147483647").readNumberOrNull()).notToBeNullBut(2147483647)
		assert(reader("-2147483648").readNumberOrNull()).notToBeNullBut(-2147483648)
		assert(reader("2147483648").readNumberOrNull()).notToBeNullBut(2147483648L)
		assert(reader("-2147483649").readNumberOrNull()).notToBeNullBut(-2147483649L)
		assert(reader("2147483647").readNumberOrNull()).notToBeNullBut(2147483647)
		assert(reader("-2147483648").readNumberOrNull()).notToBeNullBut(-2147483648)
		assert(reader("9223372036854775807").readNumberOrNull()).notToBeNullBut(9223372036854775807L)
		assert(reader("-9223372036854775808").readNumberOrNull()).notToBeNullBut(-9223372036854775807L - 1) // https://youtrack.jetbrains.com/issue/KT-17172
		assert(reader("9223372036854775808").readNumberOrNull()).notToBeNullBut(9223372036854775808.0)
		assert(reader("-9223372036854775809").readNumberOrNull()).notToBeNullBut(-9223372036854775809.0)
		assert(reader("1e2").readNumberOrNull()).notToBeNullBut(100.0)
		assert(reader("-1e2").readNumberOrNull()).notToBeNullBut(-100.0)
		assert(reader("1.0e2").readNumberOrNull()).notToBeNullBut(100.0)
		assert(reader("-1.0e2").readNumberOrNull()).notToBeNullBut(-100.0)
		assert(reader("1.0e-2").readNumberOrNull()).notToBeNullBut(0.01)
		assert(reader("-1.0e-2").readNumberOrNull()).notToBeNullBut(-0.01)
		assert(reader("1000000000000000000000000000000").readNumberOrNull()).notToBeNullBut(1000000000000000000000000000000.0)
		assert(reader("-1000000000000000000000000000000").readNumberOrNull()).notToBeNullBut(-1000000000000000000000000000000.0)
		assert(reader("1e20000").readNumberOrNull()).notToBeNullBut(Double.POSITIVE_INFINITY)
		assert(reader("-1e20000").readNumberOrNull()).notToBeNullBut(Double.NEGATIVE_INFINITY)
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
		assert(reader("0").readShortOrNull()).notToBeNullBut(0.toShort())
		assert(reader("-0").readShortOrNull()).notToBeNullBut(0.toShort())
		assert(reader("100").readShortOrNull()).notToBeNullBut(100.toShort())
		assert(reader("-100").readShortOrNull()).notToBeNullBut((-100).toShort())
		assert(reader("0.000").readShortOrNull()).notToBeNullBut(0.toShort())
		assert(reader("-0.000").readShortOrNull()).notToBeNullBut(0.toShort())
		assert(reader("100.001").readShortOrNull()).notToBeNullBut(100.toShort())
		assert(reader("-100.001").readShortOrNull()).notToBeNullBut((-100).toShort())
		assert(reader("100.999").readShortOrNull()).notToBeNullBut(100.toShort())
		assert(reader("-100.999").readShortOrNull()).notToBeNullBut((-100).toShort())
		assert(reader("1e2").readShortOrNull()).notToBeNullBut(100.toShort())
		assert(reader("-1e2").readShortOrNull()).notToBeNullBut((-100).toShort())
		assert(reader("1.0e2").readShortOrNull()).notToBeNullBut(100.toShort())
		assert(reader("-1.0e2").readShortOrNull()).notToBeNullBut((-100).toShort())
		assert(reader("1.0e+2").readShortOrNull()).notToBeNullBut(100.toShort())
		assert(reader("-1.0e+2").readShortOrNull()).notToBeNullBut((-100).toShort())
		assert(reader("1.0e-2").readShortOrNull()).notToBeNullBut(0.toShort())
		assert(reader("-1.0e-2").readShortOrNull()).notToBeNullBut(0.toShort())
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
		assert(reader("\"\"").readStringOrNull()).notToBeNullBut("")
		assert(reader("\"\\u0022\"").readStringOrNull()).notToBeNullBut("\"")
		assert(reader("\"simple\"").readStringOrNull()).notToBeNullBut("simple")
		assert(reader("\" a bit longer \"").readStringOrNull()).notToBeNullBut(" a bit longer ")
		assert(reader("\"a dog: 🐶\"").readStringOrNull()).notToBeNullBut("a dog: 🐶")
		assert(reader("\" \\\\ \\\" \\/ \\b \\f \\n \\r \\t \\uD83D\\udc36 \"").readStringOrNull()).notToBeNullBut(" \\ \" / \b \u000C \n \r \t 🐶 ")
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
				assert(readValueOrNull()).notToBeNullBut(true)
				assert(readValueOrNull()).notToBeNullBut(0)
				assert(readValueOrNull()).notToBeNullBut(emptyList<Any?>())
				assert(readValueOrNull()).notToBeNullBut(emptyMap<String, Any?>())
				assert(readValueOrNull()).notToBeNullBut("")
				readFromMap {
					assert(readValueOrNull()).notToBeNullBut("")
					assert(readValueOrNull()).notToBeNullBut(true)
				}
			}
		}
	}

	@Test
	fun testSkipValue() {
		reader("[null, 0, [], {}, \"\", {\"\":true}]").apply {
			readFromList {
				assert(nextToken).notToBeNullBut(JSONToken.nullValue)
				skipValue()
				assert(nextToken).notToBeNullBut(JSONToken.numberValue)
				skipValue()
				assert(nextToken).notToBeNullBut(JSONToken.listStart)
				skipValue()
				assert(nextToken).notToBeNullBut(JSONToken.mapStart)
				skipValue()
				assert(nextToken).notToBeNullBut(JSONToken.stringValue)
				skipValue()
				assert(nextToken).notToBeNullBut(JSONToken.mapStart)
				readFromMap {
					assert(nextToken).notToBeNullBut(JSONToken.mapKey)
					skipValue()
					assert(nextToken).notToBeNullBut(JSONToken.booleanValue)
					skipValue()
					assert(nextToken).notToBeNullBut(JSONToken.mapEnd)
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
			assert(reader("null").nextToken).notToBeNullBut(JSONToken.nullValue)
			assert(reader("true").nextToken).notToBeNullBut(JSONToken.booleanValue)
			assert(reader("false").nextToken).notToBeNullBut(JSONToken.booleanValue)
			assert(reader("0").nextToken).notToBeNullBut(JSONToken.numberValue)
			assert(reader("1").nextToken).notToBeNullBut(JSONToken.numberValue)
			assert(reader("-1.0").nextToken).notToBeNullBut(JSONToken.numberValue)
			assert(reader("\"\"").nextToken).notToBeNullBut(JSONToken.stringValue)
			reader("[]").apply {
				assert(nextToken).notToBeNullBut(JSONToken.listStart)
				readListStart()
				assert(nextToken).notToBeNullBut(JSONToken.listEnd)
			}
			reader("{}").apply {
				assert(nextToken).notToBeNullBut(JSONToken.mapStart)
				readMapStart()
				assert(nextToken).notToBeNullBut(JSONToken.mapEnd)
			}
		}


		@Test
		fun testConsumesMinimalInput() {
			assert(reader("n").nextToken).notToBeNullBut(JSONToken.nullValue)
			assert(reader("t").nextToken).notToBeNullBut(JSONToken.booleanValue)
			assert(reader("f").nextToken).notToBeNullBut(JSONToken.booleanValue)
			assert(reader("0").nextToken).notToBeNullBut(JSONToken.numberValue)
			assert(reader("1").nextToken).notToBeNullBut(JSONToken.numberValue)
			assert(reader("-").nextToken).notToBeNullBut(JSONToken.numberValue)
			assert(reader("[").nextToken).notToBeNullBut(JSONToken.listStart)
			assert(reader("\"").nextToken).notToBeNullBut(JSONToken.stringValue)
			assert(reader("{").nextToken).notToBeNullBut(JSONToken.mapStart)
		}


		@Test
		fun testIdempotency() {
			reader("n").apply { nextToken;assert(nextToken).notToBeNullBut(JSONToken.nullValue) }
			reader("t").apply { nextToken;assert(nextToken).notToBeNullBut(JSONToken.booleanValue) }
			reader("f").apply { nextToken;assert(nextToken).notToBeNullBut(JSONToken.booleanValue) }
			reader("0").apply { nextToken;assert(nextToken).notToBeNullBut(JSONToken.numberValue) }
			reader("1").apply { nextToken;assert(nextToken).notToBeNullBut(JSONToken.numberValue) }
			reader("-").apply { nextToken;assert(nextToken).notToBeNullBut(JSONToken.numberValue) }
			reader("[").apply { nextToken;assert(nextToken).notToBeNullBut(JSONToken.listStart) }
			reader("\"").apply { nextToken;assert(nextToken).notToBeNullBut(JSONToken.stringValue) }
			reader("{").apply { nextToken;assert(nextToken).notToBeNullBut(JSONToken.mapStart) }
		}


		@Test
		fun testCompletesWithNull() {
			reader("0").apply { readInt(); assert(nextToken).toBe(null) }
		}
	}


	private fun reader(reader: Reader): JSONReader =
		StandardReader(TextInput(reader))


	private fun reader(string: String): JSONReader =
		StandardReader(TextInput(StringReader(string)))


	private object Dummy1
	private object Dummy2
	private object Dummy3
}
