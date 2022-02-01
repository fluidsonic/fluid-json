package tests.basic

import io.fluidsonic.json.*
import java.io.*
import kotlin.test.*


@Suppress("RedundantAsSequence")
class StandardWriterAcceptTest {

	@Test
	fun testClose() {
		write { writeNull(); close() }
		write { writeNull(); close(); close() }
	}


	@Test
	fun testTerminate() {
		write { writeNull(); terminate() }
	}


	@Test
	fun testMarkAsErrored() {
		write {
			expect(isErrored).toBe(false)
			markAsErrored()
			expect(isErrored).toBe(true)
		}
	}


	@Test
	fun testWriteBoolean() {
		expect(write { writeBoolean(false) }).toBe("false")
		expect(write { writeBoolean(true) }).toBe("true")
	}


	@Test
	fun testWriteBooleanOrNull() {
		expect(write { writeBooleanOrNull(false) }).toBe("false")
		expect(write { writeBooleanOrNull(true) }).toBe("true")
		expect(write { writeBooleanOrNull(null) }).toBe("null")
	}


	@Test
	fun testWriteByte() {
		expect(write { writeByte(Byte.MIN_VALUE) }).toBe("-128")
		expect(write { writeByte(-1) }).toBe("-1")
		expect(write { writeByte(0) }).toBe("0")
		expect(write { writeByte(1) }).toBe("1")
		expect(write { writeByte(Byte.MAX_VALUE) }).toBe("127")
	}


	@Test
	fun testWriteByteOrNull() {
		expect(write { writeByteOrNull(Byte.MIN_VALUE) }).toBe("-128")
		expect(write { writeByteOrNull(-1) }).toBe("-1")
		expect(write { writeByteOrNull(0) }).toBe("0")
		expect(write { writeByteOrNull(1) }).toBe("1")
		expect(write { writeByteOrNull(Byte.MAX_VALUE) }).toBe("127")
		expect(write { writeByteOrNull(null) }).toBe("null")
	}


	@Test
	fun testWriteChar() {
		expect(write { writeChar(Char.MIN_VALUE) }).toBe(""""\u0000"""")
		expect(write { writeChar('a') }).toBe(""""a"""")
		expect(write { writeChar(Char.MAX_VALUE) }).toBe("\"\uFFFF\"")
	}


	@Test
	fun testWriteCharOrNull() {
		expect(write { writeCharOrNull(Char.MIN_VALUE) }).toBe(""""\u0000"""")
		expect(write { writeCharOrNull('a') }).toBe(""""a"""")
		expect(write { writeCharOrNull(Char.MAX_VALUE) }).toBe("\"\uFFFF\"")
		expect(write { writeCharOrNull(null) }).toBe("null")
	}


	@Test
	fun testWriteDouble() {
		expect(write { writeDouble(-1E200) }).toBe("-1.0E200")
		expect(write { writeDouble(-100.999) }).toBe("-100.999")
		expect(write { writeDouble(-100.001) }).toBe("-100.001")
		expect(write { writeDouble(-1E-200) }).toBe("-1.0E-200")
		expect(write { writeDouble(-0.0) }).toBe("-0.0")
		expect(write { writeDouble(0.0) }).toBe("0.0")
		expect(write { writeDouble(1E-200) }).toBe("1.0E-200")
		expect(write { writeDouble(100.001) }).toBe("100.001")
		expect(write { writeDouble(100.999) }).toBe("100.999")
		expect(write { writeDouble(1E200) }).toBe("1.0E200")
	}


	@Test
	fun testWriteDoubleOrNull() {
		expect(write { writeDoubleOrNull(-1E200) }).toBe("-1.0E200")
		expect(write { writeDoubleOrNull(-100.999) }).toBe("-100.999")
		expect(write { writeDoubleOrNull(-100.001) }).toBe("-100.001")
		expect(write { writeDoubleOrNull(-1E-200) }).toBe("-1.0E-200")
		expect(write { writeDoubleOrNull(-0.0) }).toBe("-0.0")
		expect(write { writeDoubleOrNull(0.0) }).toBe("0.0")
		expect(write { writeDoubleOrNull(1E-200) }).toBe("1.0E-200")
		expect(write { writeDoubleOrNull(100.001) }).toBe("100.001")
		expect(write { writeDoubleOrNull(100.999) }).toBe("100.999")
		expect(write { writeDoubleOrNull(1E200) }).toBe("1.0E200")
		expect(write { writeDoubleOrNull(null) }).toBe("null")
	}


	@Test
	fun testWriteFloat() {
		expect(write { writeFloat(-1E38f) }).toBe("-1.0E38")
		expect(write { writeFloat(-100.999f) }).toBe("-100.999")
		expect(write { writeFloat(-100.001f) }).toBe("-100.001")
		expect(write { writeFloat(-1E-38f) }).toBe("-1.0E-38")
		expect(write { writeFloat(-0.0f) }).toBe("-0.0")
		expect(write { writeFloat(0.0f) }).toBe("0.0")
		expect(write { writeFloat(1E-38f) }).toBe("1.0E-38")
		expect(write { writeFloat(100.001f) }).toBe("100.001")
		expect(write { writeFloat(100.999f) }).toBe("100.999")
		expect(write { writeFloat(1E38f) }).toBe("1.0E38")
	}


	@Test
	fun testWriteFloatOrNull() {
		expect(write { writeFloatOrNull(-1E38f) }).toBe("-1.0E38")
		expect(write { writeFloatOrNull(-100.999f) }).toBe("-100.999")
		expect(write { writeFloatOrNull(-100.001f) }).toBe("-100.001")
		expect(write { writeFloatOrNull(-1E-38f) }).toBe("-1.0E-38")
		expect(write { writeFloatOrNull(-0.0f) }).toBe("-0.0")
		expect(write { writeFloatOrNull(0.0f) }).toBe("0.0")
		expect(write { writeFloatOrNull(1E-38f) }).toBe("1.0E-38")
		expect(write { writeFloatOrNull(100.001f) }).toBe("100.001")
		expect(write { writeFloatOrNull(100.999f) }).toBe("100.999")
		expect(write { writeFloatOrNull(1E38f) }).toBe("1.0E38")
		expect(write { writeFloatOrNull(null) }).toBe("null")
	}


	@Test
	fun testWriteInt() {
		expect(write { writeInt(Int.MIN_VALUE) }).toBe("-2147483648")
		expect(write { writeInt(-1) }).toBe("-1")
		expect(write { writeInt(0) }).toBe("0")
		expect(write { writeInt(1) }).toBe("1")
		expect(write { writeInt(Int.MAX_VALUE) }).toBe("2147483647")
	}


	@Test
	fun testWriteIntOrNull() {
		expect(write { writeIntOrNull(Int.MIN_VALUE) }).toBe("-2147483648")
		expect(write { writeIntOrNull(-1) }).toBe("-1")
		expect(write { writeIntOrNull(0) }).toBe("0")
		expect(write { writeIntOrNull(1) }).toBe("1")
		expect(write { writeIntOrNull(Int.MAX_VALUE) }).toBe("2147483647")
		expect(write { writeIntOrNull(null) }).toBe("null")
	}


	@Test
	fun testWriteIntoList() {
		expect(write {
			writeIntoList {}
		}).toBe("[]")

		expect(write {
			writeIntoList {
				writeNull()
				writeInt(1)
			}
		}).toBe("[null,1]")
	}


	@Test
	fun testWriteIntoMap() {
		expect(write {
			writeIntoMap {}
		}).toBe("{}")

		expect(write {
			writeIntoMap {
				writeMapKey("")
				writeInt(1)
			}
		}).toBe("""{"":1}""")
	}


	@Test
	fun testWriteList() {
		expect(write { writeList(booleanArrayOf(false, true)) }).toBe("[false,true]")
		expect(write { writeList(byteArrayOf(0, 1)) }).toBe("[0,1]")
		expect(write { writeList(charArrayOf(0.toChar(), 'a')) }).toBe("""["\u0000","a"]""")
		expect(write { writeList(doubleArrayOf(0.0, 1.0)) }).toBe("[0.0,1.0]")
		expect(write { writeList(floatArrayOf(0.0f, 1.0f)) }).toBe("[0.0,1.0]")
		expect(write { writeList(intArrayOf(0, 1)) }).toBe("[0,1]")
		expect(write { writeList(longArrayOf(0, 1)) }).toBe("[0,1]")
		expect(write { writeList(shortArrayOf(0, 1)) }).toBe("[0,1]")
		expect(write { writeList(arrayOf("", "")) }).toBe("""["",""]""")
		expect(write { writeList(arrayOf("", "").asSequence().asIterable()) }).toBe("""["",""]""")
		expect(write { writeList(arrayOf("", "").asSequence()) }).toBe("""["",""]""")
	}


	@Test
	fun testWriteListByElement() {
		expect(write { writeListByElement(booleanArrayOf(false, true)) { writeValue(it) } }).toBe("[false,true]")
		expect(write { writeListByElement(byteArrayOf(0, 1)) { writeValue(it) } }).toBe("[0,1]")
		expect(write { writeListByElement(charArrayOf(0.toChar(), 'a')) { writeValue(it) } }).toBe("""["\u0000","a"]""")
		expect(write { writeListByElement(doubleArrayOf(0.0, 1.0)) { writeValue(it) } }).toBe("[0.0,1.0]")
		expect(write { writeListByElement(floatArrayOf(0.0f, 1.0f)) { writeValue(it) } }).toBe("[0.0,1.0]")
		expect(write { writeListByElement(intArrayOf(0, 1)) { writeValue(it) } }).toBe("[0,1]")
		expect(write { writeListByElement(longArrayOf(0, 1)) { writeValue(it) } }).toBe("[0,1]")
		expect(write { writeListByElement(shortArrayOf(0, 1)) { writeValue(it) } }).toBe("[0,1]")
		expect(write { writeListByElement(arrayOf("", "")) { writeValue(it) } }).toBe("""["",""]""")
		expect(write { writeListByElement(arrayOf("", "").asSequence().asIterable()) { writeValue(it) } }).toBe("""["",""]""")
		expect(write { writeListByElement(arrayOf("", "").asSequence()) { writeValue(it) } }).toBe("""["",""]""")
	}


	@Test
	fun testWriteListStartAndEnd() {
		expect(write {
			writeListStart()
			writeListEnd()
		}).toBe("[]")

		expect(write {
			writeListStart()
			writeInt(0)
			writeListEnd()
		}).toBe("[0]")

		expect(write {
			writeListStart()
			writeInt(0)
			writeInt(1); writeListEnd()
		}).toBe("[0,1]")
	}


	@Test
	fun testWriteListOrNull() {
		expect(write { writeListOrNull(booleanArrayOf(false, true)) }).toBe("[false,true]")
		expect(write { writeListOrNull(byteArrayOf(0, 1)) }).toBe("[0,1]")
		expect(write { writeListOrNull(charArrayOf(0.toChar(), 'a')) }).toBe("""["\u0000","a"]""")
		expect(write { writeListOrNull(doubleArrayOf(0.0, 1.0)) }).toBe("[0.0,1.0]")
		expect(write { writeListOrNull(floatArrayOf(0.0f, 1.0f)) }).toBe("[0.0,1.0]")
		expect(write { writeListOrNull(intArrayOf(0, 1)) }).toBe("[0,1]")
		expect(write { writeListOrNull(longArrayOf(0, 1)) }).toBe("[0,1]")
		expect(write { writeListOrNull(shortArrayOf(0, 1)) }).toBe("[0,1]")
		expect(write { writeListOrNull(arrayOf("", "")) }).toBe("""["",""]""")
		expect(write { writeListOrNull(arrayOf("", "").asSequence().asIterable()) }).toBe("""["",""]""")
		expect(write { writeListOrNull(arrayOf("", "").asSequence()) }).toBe("""["",""]""")
		expect(write { writeListOrNull(null as BooleanArray?) }).toBe("null")
		expect(write { writeListOrNull(null as ByteArray?) }).toBe("null")
		expect(write { writeListOrNull(null as CharArray?) }).toBe("null")
		expect(write { writeListOrNull(null as DoubleArray?) }).toBe("null")
		expect(write { writeListOrNull(null as FloatArray?) }).toBe("null")
		expect(write { writeListOrNull(null as IntArray?) }).toBe("null")
		expect(write { writeListOrNull(null as LongArray?) }).toBe("null")
		expect(write { writeListOrNull(null as ShortArray?) }).toBe("null")
		expect(write { writeListOrNull(null as Array<String?>?) }).toBe("null")
		expect(write { writeListOrNull(null as Iterable<String?>?) }).toBe("null")
		expect(write { writeListOrNull(null as Sequence<String?>?) }).toBe("null")
	}


	@Test
	fun testWriteListOrNullByElement() {
		expect(write { writeListOrNullByElement(booleanArrayOf(false, true)) { writeValue(it) } }).toBe("[false,true]")
		expect(write { writeListOrNullByElement(byteArrayOf(0, 1)) { writeValue(it) } }).toBe("[0,1]")
		expect(write { writeListOrNullByElement(charArrayOf(0.toChar(), 'a')) { writeValue(it) } }).toBe("""["\u0000","a"]""")
		expect(write { writeListOrNullByElement(doubleArrayOf(0.0, 1.0)) { writeValue(it) } }).toBe("[0.0,1.0]")
		expect(write { writeListOrNullByElement(floatArrayOf(0.0f, 1.0f)) { writeValue(it) } }).toBe("[0.0,1.0]")
		expect(write { writeListOrNullByElement(intArrayOf(0, 1)) { writeValue(it) } }).toBe("[0,1]")
		expect(write { writeListOrNullByElement(longArrayOf(0, 1)) { writeValue(it) } }).toBe("[0,1]")
		expect(write { writeListOrNullByElement(shortArrayOf(0, 1)) { writeValue(it) } }).toBe("[0,1]")
		expect(write { writeListOrNullByElement(arrayOf("", "")) { writeValue(it) } }).toBe("""["",""]""")
		expect(write { writeListOrNullByElement(arrayOf("", "").asSequence().asIterable()) { writeValue(it) } }).toBe("""["",""]""")
		expect(write { writeListOrNullByElement(arrayOf("", "").asSequence()) { writeValue(it) } }).toBe("""["",""]""")
		expect(write { writeListOrNullByElement(null as BooleanArray?) {} }).toBe("null")
		expect(write { writeListOrNullByElement(null as ByteArray?) {} }).toBe("null")
		expect(write { writeListOrNullByElement(null as CharArray?) {} }).toBe("null")
		expect(write { writeListOrNullByElement(null as DoubleArray?) {} }).toBe("null")
		expect(write { writeListOrNullByElement(null as FloatArray?) {} }).toBe("null")
		expect(write { writeListOrNullByElement(null as IntArray?) {} }).toBe("null")
		expect(write { writeListOrNullByElement(null as LongArray?) {} }).toBe("null")
		expect(write { writeListOrNullByElement(null as ShortArray?) {} }).toBe("null")
		expect(write { writeListOrNullByElement(null as Array<String?>?) {} }).toBe("null")
		expect(write { writeListOrNullByElement(null as Iterable<String?>?) {} }).toBe("null")
		expect(write { writeListOrNullByElement(null as Sequence<String?>?) {} }).toBe("null")
	}


	@Test
	fun testWriteLong() {
		expect(write { writeLong(Long.MIN_VALUE) }).toBe("-9223372036854775808")
		expect(write { writeLong(-1) }).toBe("-1")
		expect(write { writeLong(0) }).toBe("0")
		expect(write { writeLong(1) }).toBe("1")
		expect(write { writeLong(Long.MAX_VALUE) }).toBe("9223372036854775807")
	}


	@Test
	fun testWriteLongOrNull() {
		expect(write { writeLongOrNull(Long.MIN_VALUE) }).toBe("-9223372036854775808")
		expect(write { writeLongOrNull(-1) }).toBe("-1")
		expect(write { writeLongOrNull(0) }).toBe("0")
		expect(write { writeLongOrNull(1) }).toBe("1")
		expect(write { writeLongOrNull(Long.MAX_VALUE) }).toBe("9223372036854775807")
		expect(write { writeLongOrNull(null) }).toBe("null")
	}


	@Test
	fun testWriteMap() {
		expect(write { writeMap(mapOf("0" to 0, "1" to 1)) }).toBe("""{"0":0,"1":1}""")
	}


	@Test
	fun testWriteMapByElement() {
		expect(write {
			writeMapByElement(mapOf("0" to 0, "1" to 1)) { key, value ->
				writeMapKey(key)
				writeInt(value)
			}
		}).toBe("""{"0":0,"1":1}""")
	}


	@Test
	fun testWriteMapByElementValue() {
		expect(write {
			writeMapByElementValue(mapOf("0" to 0, "1" to 1)) { value ->
				writeInt(value)
			}
		}).toBe("""{"0":0,"1":1}""")
	}


	@Test
	fun testWriteMapElement() {
		expect(write {
			writeIntoMap {
				writeMapElement("", boolean = true)
				writeMapElement("", boolean = true, skipIfNull = false)
				writeMapElement("", boolean = true, skipIfNull = true)
				writeMapElement("", boolean = null, skipIfNull = false)
				writeMapElement("", boolean = null, skipIfNull = true)

				writeMapElement("", byte = 0)
				writeMapElement("", byte = 0, skipIfNull = false)
				writeMapElement("", byte = 0, skipIfNull = true)
				writeMapElement("", byte = null, skipIfNull = false)
				writeMapElement("", byte = null, skipIfNull = true)

				writeMapElement("", char = 0.toChar())
				writeMapElement("", char = 0.toChar(), skipIfNull = false)
				writeMapElement("", char = 0.toChar(), skipIfNull = true)
				writeMapElement("", char = null, skipIfNull = false)
				writeMapElement("", char = null, skipIfNull = true)

				writeMapElement("", double = 0.0)
				writeMapElement("", double = 0.0, skipIfNull = false)
				writeMapElement("", double = 0.0, skipIfNull = true)
				writeMapElement("", double = null, skipIfNull = false)
				writeMapElement("", double = null, skipIfNull = true)

				writeMapElement("", float = 0.0f)
				writeMapElement("", float = 0.0f, skipIfNull = false)
				writeMapElement("", float = 0.0f, skipIfNull = true)
				writeMapElement("", float = null, skipIfNull = false)
				writeMapElement("", float = null, skipIfNull = true)

				writeMapElement("", int = 0)
				writeMapElement("", int = 0, skipIfNull = false)
				writeMapElement("", int = 0, skipIfNull = true)
				writeMapElement("", int = null, skipIfNull = false)
				writeMapElement("", int = null, skipIfNull = true)

				writeMapElement("", value = arrayOf(""))
				writeMapElement("", value = arrayOf(""), skipIfNull = false)
				writeMapElement("", value = arrayOf(""), skipIfNull = true)
				writeMapElement("", value = null as Array<*>?, skipIfNull = false)
				writeMapElement("", value = null as Array<*>?, skipIfNull = true)

				writeMapElement("", value = booleanArrayOf(true))
				writeMapElement("", value = booleanArrayOf(true), skipIfNull = false)
				writeMapElement("", value = booleanArrayOf(true), skipIfNull = true)
				writeMapElement("", value = null as BooleanArray?, skipIfNull = false)
				writeMapElement("", value = null as BooleanArray?, skipIfNull = true)

				writeMapElement("", value = byteArrayOf(0))
				writeMapElement("", value = byteArrayOf(0), skipIfNull = false)
				writeMapElement("", value = byteArrayOf(0), skipIfNull = true)
				writeMapElement("", value = null as ByteArray?, skipIfNull = false)
				writeMapElement("", value = null as ByteArray?, skipIfNull = true)

				writeMapElement("", value = charArrayOf(0.toChar()))
				writeMapElement("", value = charArrayOf(0.toChar()), skipIfNull = false)
				writeMapElement("", value = charArrayOf(0.toChar()), skipIfNull = true)
				writeMapElement("", value = null as CharArray?, skipIfNull = false)
				writeMapElement("", value = null as CharArray?, skipIfNull = true)

				writeMapElement("", value = doubleArrayOf(0.0))
				writeMapElement("", value = doubleArrayOf(0.0), skipIfNull = false)
				writeMapElement("", value = doubleArrayOf(0.0), skipIfNull = true)
				writeMapElement("", value = null as DoubleArray?, skipIfNull = false)
				writeMapElement("", value = null as DoubleArray?, skipIfNull = true)

				writeMapElement("", value = floatArrayOf(0.0f))
				writeMapElement("", value = floatArrayOf(0.0f), skipIfNull = false)
				writeMapElement("", value = floatArrayOf(0.0f), skipIfNull = true)
				writeMapElement("", value = null as FloatArray?, skipIfNull = false)
				writeMapElement("", value = null as FloatArray?, skipIfNull = true)

				writeMapElement("", value = intArrayOf(0))
				writeMapElement("", value = intArrayOf(0), skipIfNull = false)
				writeMapElement("", value = intArrayOf(0), skipIfNull = true)
				writeMapElement("", value = null as IntArray?, skipIfNull = false)
				writeMapElement("", value = null as IntArray?, skipIfNull = true)

				writeMapElement("", value = listOf("") as Iterable<*>)
				writeMapElement("", value = listOf("") as Iterable<*>, skipIfNull = false)
				writeMapElement("", value = listOf("") as Iterable<*>, skipIfNull = true)
				writeMapElement("", value = null as Iterable<*>?, skipIfNull = false)
				writeMapElement("", value = null as Iterable<*>?, skipIfNull = true)

				writeMapElement("", list = listOf(0) as Iterable<Int>) { writeInt(it + 1) }
				writeMapElement("", list = listOf(0) as Iterable<Int>, skipIfNull = false) { writeInt(it + 1) }
				writeMapElement("", list = listOf(0) as Iterable<Int>, skipIfNull = true) { writeInt(it + 1) }
				writeMapElement("", list = null as Iterable<Int>?, skipIfNull = false) { writeInt(it + 1) }
				writeMapElement("", list = null as Iterable<Int>?, skipIfNull = true) { writeInt(it + 1) }

				writeMapElement("", value = longArrayOf(0))
				writeMapElement("", value = longArrayOf(0), skipIfNull = false)
				writeMapElement("", value = longArrayOf(0), skipIfNull = true)
				writeMapElement("", value = null as LongArray?, skipIfNull = false)
				writeMapElement("", value = null as LongArray?, skipIfNull = true)

				writeMapElement("", value = listOf("").asSequence())
				writeMapElement("", value = listOf("").asSequence(), skipIfNull = false)
				writeMapElement("", value = listOf("").asSequence(), skipIfNull = true)
				writeMapElement("", value = null as Sequence<*>?, skipIfNull = false)
				writeMapElement("", value = null as Sequence<*>?, skipIfNull = true)

				writeMapElement("", list = listOf(0).asSequence()) { writeInt(it + 1) }
				writeMapElement("", list = listOf(0).asSequence(), skipIfNull = false) { writeInt(it + 1) }
				writeMapElement("", list = listOf(0).asSequence(), skipIfNull = true) { writeInt(it + 1) }
				writeMapElement("", list = null as Sequence<Int>?, skipIfNull = false) { writeInt(it + 1) }
				writeMapElement("", list = null as Sequence<Int>?, skipIfNull = true) { writeInt(it + 1) }

				writeMapElement("", value = shortArrayOf(0))
				writeMapElement("", value = shortArrayOf(0), skipIfNull = false)
				writeMapElement("", value = shortArrayOf(0), skipIfNull = true)
				writeMapElement("", value = null as ShortArray?, skipIfNull = false)
				writeMapElement("", value = null as ShortArray?, skipIfNull = true)

				writeMapElement("", long = 0)
				writeMapElement("", long = 0, skipIfNull = false)
				writeMapElement("", long = 0, skipIfNull = true)
				writeMapElement("", long = null, skipIfNull = false)
				writeMapElement("", long = null, skipIfNull = true)

				writeMapElement("", value = mapOf("" to 0) as Map<*, *>)
				writeMapElement("", value = mapOf("" to 0) as Map<*, *>, skipIfNull = false)
				writeMapElement("", value = mapOf("" to 0) as Map<*, *>, skipIfNull = true)
				writeMapElement("", value = null as Map<*, *>?, skipIfNull = false)
				writeMapElement("", value = null as Map<*, *>?, skipIfNull = true)

				writeMapElement("", map = mapOf("" to 0) as Map<*, Int>) { writeInt(it + 1) }
				writeMapElement("", map = mapOf("" to 0) as Map<*, Int>, skipIfNull = false) { writeInt(it + 1) }
				writeMapElement("", map = mapOf("" to 0) as Map<*, Int>, skipIfNull = true) { writeInt(it + 1) }
				writeMapElement("", map = null as Map<*, Int>?, skipIfNull = false) { writeInt(it + 1) }
				writeMapElement("", map = null as Map<*, Int>?, skipIfNull = true) { writeInt(it + 1) }

				writeMapElement("", number = 0)
				writeMapElement("", number = 0, skipIfNull = false)
				writeMapElement("", number = 0, skipIfNull = true)
				writeMapElement("", number = null, skipIfNull = false)
				writeMapElement("", number = null, skipIfNull = true)

				writeMapElement("", short = 0)
				writeMapElement("", short = 0, skipIfNull = false)
				writeMapElement("", short = 0, skipIfNull = true)
				writeMapElement("", short = null, skipIfNull = false)
				writeMapElement("", short = null, skipIfNull = true)

				writeMapElement("", string = "")
				writeMapElement("", string = "", skipIfNull = false)
				writeMapElement("", string = "", skipIfNull = true)
				writeMapElement("", string = null, skipIfNull = false)
				writeMapElement("", string = null, skipIfNull = true)

				writeMapElement("", value = "")
				writeMapElement("", value = "", skipIfNull = false)
				writeMapElement("", value = "", skipIfNull = true)
				writeMapElement("", value = null, skipIfNull = false)
				writeMapElement("", value = null, skipIfNull = true)

				writeMapElement("", value = 0) { writeInt(it + 1) }
				writeMapElement("", value = 0, skipIfNull = false) { writeInt(it + 1) }
				writeMapElement("", value = 0, skipIfNull = true) { writeInt(it + 1) }
				writeMapElement("", value = null as Int?, skipIfNull = false) { writeInt(it + 1) }
				writeMapElement("", value = null as Int?, skipIfNull = true) { writeInt(it + 1) }

				writeMapElement("") { writeString("inline") }

				writeMapNullElement("")
			}
		}).toBe("""
				{
					"": true,
					"": true,
					"": true,
					"": null,

					"": 0,
					"": 0,
					"": 0,
					"": null,

					"": "\u0000",
					"": "\u0000",
					"": "\u0000",
					"": null,

					"": 0.0,
					"": 0.0,
					"": 0.0,
					"": null,

					"": 0.0,
					"": 0.0,
					"": 0.0,
					"": null,

					"": 0,
					"": 0,
					"": 0,
					"": null,

					"": [""],
					"": [""],
					"": [""],
					"": null,

					"": [true],
					"": [true],
					"": [true],
					"": null,

					"": [0],
					"": [0],
					"": [0],
					"": null,

					"": ["\u0000"],
					"": ["\u0000"],
					"": ["\u0000"],
					"": null,

					"": [0.0],
					"": [0.0],
					"": [0.0],
					"": null,

					"": [0.0],
					"": [0.0],
					"": [0.0],
					"": null,

					"": [0],
					"": [0],
					"": [0],
					"": null,

					"": [""],
					"": [""],
					"": [""],
					"": null,

					"": [1],
					"": [1],
					"": [1],
					"": null,

					"": [0],
					"": [0],
					"": [0],
					"": null,

					"": [""],
					"": [""],
					"": [""],
					"": null,

					"": [1],
					"": [1],
					"": [1],
					"": null,

					"": [0],
					"": [0],
					"": [0],
					"": null,

					"": 0,
					"": 0,
					"": 0,
					"": null,

					"": {"":0},
					"": {"":0},
					"": {"":0},
					"": null,

					"": {"":1},
					"": {"":1},
					"": {"":1},
					"": null,

					"": 0,
					"": 0,
					"": 0,
					"": null,

					"": 0,
					"": 0,
					"": 0,
					"": null,

					"": "",
					"": "",
					"": "",
					"": null,

					"": "",
					"": "",
					"": "",
					"": null,

					"": 1,
					"": 1,
					"": 1,
					"": null,

					"": "inline",

					"": null
				}
				""".filterNot(Char::isWhitespace)
		)
	}


	@Test
	fun testWriteMapStartAndEnd() {
		expect(write {
			writeMapStart()
			writeMapEnd()
		}).toBe("{}")

		expect(write {
			writeMapStart()
			writeMapKey("0")
			writeInt(0)
			writeMapEnd()
		}).toBe("""{"0":0}""")

		expect(write {
			writeMapStart()
			writeMapKey("0")
			writeInt(0)
			writeMapKey("1")
			writeInt(1)
			writeMapEnd()
		}).toBe("""{"0":0,"1":1}""")
	}


	@Test
	fun testWriteMapKey() {
		expect(write {
			writeMapStart()
			writeMapKey("")
			writeInt(0)
			writeMapEnd()
		}).toBe("""{"":0}""")

		expect(write {
			writeMapStart()
			writeMapKey("simple")
			writeInt(0)
			writeMapEnd()
		}).toBe("""{"simple":0}""")

		expect(write {
			writeMapStart()
			writeMapKey("emoji: 🐶")
			writeInt(0)
			writeMapEnd()
		}).toBe("""{"emoji: 🐶":0}""")

		expect(write {
			writeMapStart()
			writeMapKey("\\ \"")
			writeInt(0)
			writeMapEnd()
		}).toBe("""{"\\ \"":0}""")

		expect(write {
			writeMapStart()
			writeMapKey("\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\u000B\u000E\u000F\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001A\u001B\u001C\u001D\u001E\u001F\u0020")
			writeInt(0)
			writeMapEnd()
		}).toBe("""{"\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\u000B\u000E\u000F\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001A\u001B\u001C\u001D\u001E\u001F ":0}""")

		expect(write {
			writeMapStart()
			writeMapKey(" \\ \" / \b \u000C \n \r \t ")
			writeInt(0)
			writeMapEnd()
		}).toBe("""{" \\ \" / \b \f \n \r \t ":0}""")
	}


	@Test
	fun testWriteMapOrNull() {
		expect(write { writeMapOrNull(mapOf("0" to 0, "1" to 1)) }).toBe("""{"0":0,"1":1}""")
		expect(write { writeMapOrNull(null) }).toBe("null")
	}


	@Test
	fun testWriteMapOrNullByElement() {
		expect(write {
			writeMapOrNullByElement(mapOf("0" to 0, "1" to 1)) { key, value ->
				writeMapKey(key)
				writeInt(value)
			}
		}).toBe("""{"0":0,"1":1}""")

		expect(write {
			writeMapOrNullByElement(null as Map<String, Int>?) { _, _ -> }
		}).toBe("null")
	}


	@Test
	fun testWriteMapOrNullByElementValue() {
		expect(write {
			writeMapOrNullByElementValue(mapOf("0" to 0, "1" to 1)) { value ->
				writeInt(value)
			}
		}).toBe("""{"0":0,"1":1}""")

		expect(write {
			writeMapOrNullByElementValue(null as Map<String, Int>?) {}
		}).toBe("null")
	}


	@Test
	fun testWriteNull() {
		expect(write { writeNull() }).toBe("null")
	}


	@Test
	fun testWriteNumber() {
		expect(write { writeNumber(Long.MIN_VALUE) }).toBe("-9223372036854775808")
		expect(write { writeNumber(Int.MIN_VALUE - 1L) }).toBe("-2147483649")
		expect(write { writeNumber(Int.MIN_VALUE) }).toBe("-2147483648")
		expect(write { writeNumber(-1) }).toBe("-1")
		expect(write { writeNumber(0) }).toBe("0")
		expect(write { writeNumber(1) }).toBe("1")
		expect(write { writeNumber(Int.MAX_VALUE) }).toBe("2147483647")
		expect(write { writeNumber(Int.MAX_VALUE + 1L) }).toBe("2147483648")
		expect(write { writeNumber(Long.MAX_VALUE) }).toBe("9223372036854775807")
		expect(write { writeNumber(-1E200) }).toBe("-1.0E200")
		expect(write { writeNumber(-100.999) }).toBe("-100.999")
		expect(write { writeNumber(-100.001) }).toBe("-100.001")
		expect(write { writeNumber(-1E-200) }).toBe("-1.0E-200")
		expect(write { writeNumber(-0.0) }).toBe("-0.0")
		expect(write { writeNumber(0.0) }).toBe("0.0")
		expect(write { writeNumber(1E-200) }).toBe("1.0E-200")
		expect(write { writeNumber(100.001) }).toBe("100.001")
		expect(write { writeNumber(100.999) }).toBe("100.999")
		expect(write { writeNumber(1E200) }).toBe("1.0E200")
	}


	@Test
	fun testWriteNumberOrNull() {
		expect(write { writeNumberOrNull(Long.MIN_VALUE) }).toBe("-9223372036854775808")
		expect(write { writeNumberOrNull(Int.MIN_VALUE - 1L) }).toBe("-2147483649")
		expect(write { writeNumberOrNull(Int.MIN_VALUE) }).toBe("-2147483648")
		expect(write { writeNumberOrNull(-1) }).toBe("-1")
		expect(write { writeNumberOrNull(0) }).toBe("0")
		expect(write { writeNumberOrNull(1) }).toBe("1")
		expect(write { writeNumberOrNull(Int.MAX_VALUE) }).toBe("2147483647")
		expect(write { writeNumberOrNull(Int.MAX_VALUE + 1L) }).toBe("2147483648")
		expect(write { writeNumberOrNull(Long.MAX_VALUE) }).toBe("9223372036854775807")
		expect(write { writeNumberOrNull(-1E200) }).toBe("-1.0E200")
		expect(write { writeNumberOrNull(-100.999) }).toBe("-100.999")
		expect(write { writeNumberOrNull(-100.001) }).toBe("-100.001")
		expect(write { writeNumberOrNull(-1E-200) }).toBe("-1.0E-200")
		expect(write { writeNumberOrNull(-0.0) }).toBe("-0.0")
		expect(write { writeNumberOrNull(0.0) }).toBe("0.0")
		expect(write { writeNumberOrNull(1E-200) }).toBe("1.0E-200")
		expect(write { writeNumberOrNull(100.001) }).toBe("100.001")
		expect(write { writeNumberOrNull(100.999) }).toBe("100.999")
		expect(write { writeNumberOrNull(1E200) }).toBe("1.0E200")
		expect(write { writeNumberOrNull(null) }).toBe("null")
	}


	@Test
	fun testWriteShort() {
		expect(write { writeShort(Short.MIN_VALUE) }).toBe("-32768")
		expect(write { writeShort(-1) }).toBe("-1")
		expect(write { writeShort(0) }).toBe("0")
		expect(write { writeShort(1) }).toBe("1")
		expect(write { writeShort(Short.MAX_VALUE) }).toBe("32767")
	}


	@Test
	fun testWriteShortOrNull() {
		expect(write { writeShortOrNull(Short.MIN_VALUE) }).toBe("-32768")
		expect(write { writeShortOrNull(-1) }).toBe("-1")
		expect(write { writeShortOrNull(0) }).toBe("0")
		expect(write { writeShortOrNull(1) }).toBe("1")
		expect(write { writeShortOrNull(Short.MAX_VALUE) }).toBe("32767")
		expect(write { writeShortOrNull(null) }).toBe("null")
	}


	@Test
	fun testWriteString() {
		expect(write { writeString("") }).toBe("\"\"")
		expect(write { writeString("simple") }).toBe("\"simple\"")
		expect(write { writeString("emoji: 🐶") }).toBe("\"emoji: 🐶\"")
		expect(write { writeString("\\ \"") }).toBe("\"\\\\ \\\"\"")
		expect(write { writeString("\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\u000B\u000E\u000F\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001A\u001B\u001C\u001D\u001E\u001F\u0020") }).toBe("\"\\u0000\\u0001\\u0002\\u0003\\u0004\\u0005\\u0006\\u0007\\u000B\\u000E\\u000F\\u0010\\u0011\\u0012\\u0013\\u0014\\u0015\\u0016\\u0017\\u0018\\u0019\\u001A\\u001B\\u001C\\u001D\\u001E\\u001F \"")
		expect(write { writeString(" \\ \" / \b \u000C \n \r \t ") }).toBe("\" \\\\ \\\" / \\b \\f \\n \\r \\t \"")
	}


	@Test
	fun testWriteStringOrNull() {
		expect(write { writeStringOrNull("") }).toBe("\"\"")
		expect(write { writeStringOrNull("simple") }).toBe("\"simple\"")
		expect(write { writeStringOrNull("emoji: 🐶") }).toBe("\"emoji: 🐶\"")
		expect(write { writeStringOrNull("\\ \"") }).toBe("\"\\\\ \\\"\"")
		expect(write { writeStringOrNull("\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\u000B\u000E\u000F\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001A\u001B\u001C\u001D\u001E\u001F\u0020") }).toBe("\"\\u0000\\u0001\\u0002\\u0003\\u0004\\u0005\\u0006\\u0007\\u000B\\u000E\\u000F\\u0010\\u0011\\u0012\\u0013\\u0014\\u0015\\u0016\\u0017\\u0018\\u0019\\u001A\\u001B\\u001C\\u001D\\u001E\\u001F \"")
		expect(write { writeStringOrNull(" \\ \" / \b \u000C \n \r \t ") }).toBe("\" \\\\ \\\" / \\b \\f \\n \\r \\t \"")
		expect(write { writeStringOrNull(null) }).toBe("null")
	}


	@Test
	fun testWriteValue() {
		expect(write {
			writeIntoList {
				writeValue(arrayOf("", ""))
				writeValue(true)
				writeValue(booleanArrayOf(true, true))
				writeValue(1.toByte())
				writeValue(byteArrayOf(0, 1))
				writeValue(0.toChar())
				writeValue(charArrayOf(0.toChar(), 'a'))
				writeValue(1.0)
				writeValue(doubleArrayOf(0.0, 1.0))
				writeValue(1.0f)
				writeValue(floatArrayOf(0.0f, 1.0f))
				writeValue(1)
				writeValue(intArrayOf(0, 1))
				writeValue(booleanArrayOf(true, true).asSequence().asIterable())
				writeValue(1L)
				writeValue(longArrayOf(0, 1))
				writeValue(mapOf("0" to 0, "1" to 1))
				writeValue(booleanArrayOf(true, true).asSequence())
				writeValue(1.toShort())
				writeValue(shortArrayOf(0, 1))
				writeValue("")
				writeValue(1.toBigDecimal())
			}
		}).toBe("""[["",""],true,[true,true],1,[0,1],"\u0000",["\u0000","a"],1.0,[0.0,1.0],1.0,[0.0,1.0],1,[0,1],[true,true],1,[0,1],{"0":0,"1":1},[true,true],1,[0,1],"",1.0]""")
	}


	@Test
	fun testWriteValueOrNull() {
		expect(write {
			writeIntoList {
				writeValueOrNull(arrayOf("", ""))
				writeValueOrNull(true)
				writeValueOrNull(booleanArrayOf(true, true))
				writeValueOrNull(1.toByte())
				writeValueOrNull(byteArrayOf(0, 1))
				writeValueOrNull(0.toChar())
				writeValueOrNull(charArrayOf(0.toChar(), 'a'))
				writeValueOrNull(1.0)
				writeValueOrNull(doubleArrayOf(0.0, 1.0))
				writeValueOrNull(1.0f)
				writeValueOrNull(floatArrayOf(0.0f, 1.0f))
				writeValueOrNull(1)
				writeValueOrNull(intArrayOf(0, 1))
				writeValueOrNull(booleanArrayOf(true, true).asSequence().asIterable())
				writeValueOrNull(1L)
				writeValueOrNull(longArrayOf(0, 1))
				writeValueOrNull(mapOf("0" to 0, "1" to 1))
				writeValueOrNull(booleanArrayOf(true, true).asSequence())
				writeValueOrNull(1.toShort())
				writeValueOrNull(shortArrayOf(0, 1))
				writeValueOrNull("")
				writeValueOrNull(1.toBigDecimal())
				writeValueOrNull(null)
			}
		}).toBe("""[["",""],true,[true,true],1,[0,1],"\u0000",["\u0000","a"],1.0,[0.0,1.0],1.0,[0.0,1.0],1,[0,1],[true,true],1,[0,1],{"0":0,"1":1},[true,true],1,[0,1],"",1.0,null]""")
	}


	@Test
	fun testWriteValueAsMapKey() {
		expect(write {
			writeIntoMap {
				writeValue("hey")
				writeNull()
			}
		}).toBe("""{"hey":null}""")
	}


	private fun write(block: JsonWriter.() -> Unit): String =
		StringWriter().also { JsonWriter.build(it).block() }.toString()
}
