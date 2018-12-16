package tests.basic

import ch.tutteli.atrium.api.cc.en_GB.toBe
import ch.tutteli.atrium.verbs.assert
import com.github.fluidsonic.fluid.json.*
import org.junit.jupiter.api.Test
import java.io.StringWriter


internal object StandardWriterAcceptTest {

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
			assert(isErrored).toBe(false)
			markAsErrored()
			assert(isErrored).toBe(true)
		}
	}


	@Test
	fun testWriteBoolean() {
		assert(write { writeBoolean(false) }).toBe("false")
		assert(write { writeBoolean(true) }).toBe("true")
	}


	@Test
	fun testWriteBooleanOrNull() {
		assert(write { writeBooleanOrNull(false) }).toBe("false")
		assert(write { writeBooleanOrNull(true) }).toBe("true")
		assert(write { writeBooleanOrNull(null) }).toBe("null")
	}


	@Test
	fun testWriteByte() {
		assert(write { writeByte(Byte.MIN_VALUE) }).toBe("-128")
		assert(write { writeByte(-1) }).toBe("-1")
		assert(write { writeByte(0) }).toBe("0")
		assert(write { writeByte(1) }).toBe("1")
		assert(write { writeByte(Byte.MAX_VALUE) }).toBe("127")
	}


	@Test
	fun testWriteByteOrNull() {
		assert(write { writeByteOrNull(Byte.MIN_VALUE) }).toBe("-128")
		assert(write { writeByteOrNull(-1) }).toBe("-1")
		assert(write { writeByteOrNull(0) }).toBe("0")
		assert(write { writeByteOrNull(1) }).toBe("1")
		assert(write { writeByteOrNull(Byte.MAX_VALUE) }).toBe("127")
		assert(write { writeByteOrNull(null) }).toBe("null")
	}


	@Test
	fun testWriteChar() {
		assert(write { writeChar(Char.MIN_VALUE) }).toBe(""""\u0000"""")
		assert(write { writeChar('a') }).toBe(""""a"""")
		assert(write { writeChar(Char.MAX_VALUE) }).toBe("\"\uFFFF\"")
	}


	@Test
	fun testWriteCharOrNull() {
		assert(write { writeCharOrNull(Char.MIN_VALUE) }).toBe(""""\u0000"""")
		assert(write { writeCharOrNull('a') }).toBe(""""a"""")
		assert(write { writeCharOrNull(Char.MAX_VALUE) }).toBe("\"\uFFFF\"")
		assert(write { writeCharOrNull(null) }).toBe("null")
	}


	@Test
	fun testWriteDouble() {
		assert(write { writeDouble(-1E200) }).toBe("-1.0E200")
		assert(write { writeDouble(-100.999) }).toBe("-100.999")
		assert(write { writeDouble(-100.001) }).toBe("-100.001")
		assert(write { writeDouble(-1E-200) }).toBe("-1.0E-200")
		assert(write { writeDouble(-0.0) }).toBe("-0.0")
		assert(write { writeDouble(0.0) }).toBe("0.0")
		assert(write { writeDouble(1E-200) }).toBe("1.0E-200")
		assert(write { writeDouble(100.001) }).toBe("100.001")
		assert(write { writeDouble(100.999) }).toBe("100.999")
		assert(write { writeDouble(1E200) }).toBe("1.0E200")
	}


	@Test
	fun testWriteDoubleOrNull() {
		assert(write { writeDoubleOrNull(-1E200) }).toBe("-1.0E200")
		assert(write { writeDoubleOrNull(-100.999) }).toBe("-100.999")
		assert(write { writeDoubleOrNull(-100.001) }).toBe("-100.001")
		assert(write { writeDoubleOrNull(-1E-200) }).toBe("-1.0E-200")
		assert(write { writeDoubleOrNull(-0.0) }).toBe("-0.0")
		assert(write { writeDoubleOrNull(0.0) }).toBe("0.0")
		assert(write { writeDoubleOrNull(1E-200) }).toBe("1.0E-200")
		assert(write { writeDoubleOrNull(100.001) }).toBe("100.001")
		assert(write { writeDoubleOrNull(100.999) }).toBe("100.999")
		assert(write { writeDoubleOrNull(1E200) }).toBe("1.0E200")
		assert(write { writeDoubleOrNull(null) }).toBe("null")
	}


	@Test
	fun testWriteFloat() {
		assert(write { writeFloat(-1E38f) }).toBe("-1.0E38")
		assert(write { writeFloat(-100.999f) }).toBe("-100.999")
		assert(write { writeFloat(-100.001f) }).toBe("-100.001")
		assert(write { writeFloat(-1E-38f) }).toBe("-1.0E-38")
		assert(write { writeFloat(-0.0f) }).toBe("-0.0")
		assert(write { writeFloat(0.0f) }).toBe("0.0")
		assert(write { writeFloat(1E-38f) }).toBe("1.0E-38")
		assert(write { writeFloat(100.001f) }).toBe("100.001")
		assert(write { writeFloat(100.999f) }).toBe("100.999")
		assert(write { writeFloat(1E38f) }).toBe("1.0E38")
	}


	@Test
	fun testWriteFloatOrNull() {
		assert(write { writeFloatOrNull(-1E38f) }).toBe("-1.0E38")
		assert(write { writeFloatOrNull(-100.999f) }).toBe("-100.999")
		assert(write { writeFloatOrNull(-100.001f) }).toBe("-100.001")
		assert(write { writeFloatOrNull(-1E-38f) }).toBe("-1.0E-38")
		assert(write { writeFloatOrNull(-0.0f) }).toBe("-0.0")
		assert(write { writeFloatOrNull(0.0f) }).toBe("0.0")
		assert(write { writeFloatOrNull(1E-38f) }).toBe("1.0E-38")
		assert(write { writeFloatOrNull(100.001f) }).toBe("100.001")
		assert(write { writeFloatOrNull(100.999f) }).toBe("100.999")
		assert(write { writeFloatOrNull(1E38f) }).toBe("1.0E38")
		assert(write { writeFloatOrNull(null) }).toBe("null")
	}


	@Test
	fun testWriteInt() {
		assert(write { writeInt(Int.MIN_VALUE) }).toBe("-2147483648")
		assert(write { writeInt(-1) }).toBe("-1")
		assert(write { writeInt(0) }).toBe("0")
		assert(write { writeInt(1) }).toBe("1")
		assert(write { writeInt(Int.MAX_VALUE) }).toBe("2147483647")
	}


	@Test
	fun testWriteIntOrNull() {
		assert(write { writeIntOrNull(Int.MIN_VALUE) }).toBe("-2147483648")
		assert(write { writeIntOrNull(-1) }).toBe("-1")
		assert(write { writeIntOrNull(0) }).toBe("0")
		assert(write { writeIntOrNull(1) }).toBe("1")
		assert(write { writeIntOrNull(Int.MAX_VALUE) }).toBe("2147483647")
		assert(write { writeIntOrNull(null) }).toBe("null")
	}


	@Test
	fun testWriteIntoList() {
		assert(write {
			writeIntoList {}
		}).toBe("[]")

		assert(write {
			writeIntoList {
				writeNull()
				writeInt(1)
			}
		}).toBe("[null,1]")
	}


	@Test
	fun testWriteIntoMap() {
		assert(write {
			writeIntoMap {}
		}).toBe("{}")

		assert(write {
			writeIntoMap {
				writeMapKey("")
				writeInt(1)
			}
		}).toBe("""{"":1}""")
	}


	@Test
	fun testWriteList() {
		assert(write { writeList(booleanArrayOf(false, true)) }).toBe("[false,true]")
		assert(write { writeList(byteArrayOf(0, 1)) }).toBe("[0,1]")
		assert(write { writeList(charArrayOf(0.toChar(), 'a')) }).toBe("""["\u0000","a"]""")
		assert(write { writeList(doubleArrayOf(0.0, 1.0)) }).toBe("[0.0,1.0]")
		assert(write { writeList(floatArrayOf(0.0f, 1.0f)) }).toBe("[0.0,1.0]")
		assert(write { writeList(intArrayOf(0, 1)) }).toBe("[0,1]")
		assert(write { writeList(longArrayOf(0, 1)) }).toBe("[0,1]")
		assert(write { writeList(shortArrayOf(0, 1)) }).toBe("[0,1]")
		assert(write { writeList(arrayOf("", "")) }).toBe("""["",""]""")
		assert(write { writeList(arrayOf("", "").asSequence().asIterable()) }).toBe("""["",""]""")
		assert(write { writeList(arrayOf("", "").asSequence()) }).toBe("""["",""]""")
	}


	@Test
	fun testWriteListByElement() {
		assert(write { writeListByElement(booleanArrayOf(false, true)) { writeValue(it) } }).toBe("[false,true]")
		assert(write { writeListByElement(byteArrayOf(0, 1)) { writeValue(it) } }).toBe("[0,1]")
		assert(write { writeListByElement(charArrayOf(0.toChar(), 'a')) { writeValue(it) } }).toBe("""["\u0000","a"]""")
		assert(write { writeListByElement(doubleArrayOf(0.0, 1.0)) { writeValue(it) } }).toBe("[0.0,1.0]")
		assert(write { writeListByElement(floatArrayOf(0.0f, 1.0f)) { writeValue(it) } }).toBe("[0.0,1.0]")
		assert(write { writeListByElement(intArrayOf(0, 1)) { writeValue(it) } }).toBe("[0,1]")
		assert(write { writeListByElement(longArrayOf(0, 1)) { writeValue(it) } }).toBe("[0,1]")
		assert(write { writeListByElement(shortArrayOf(0, 1)) { writeValue(it) } }).toBe("[0,1]")
		assert(write { writeListByElement(arrayOf("", "")) { writeValue(it) } }).toBe("""["",""]""")
		assert(write { writeListByElement(arrayOf("", "").asSequence().asIterable()) { writeValue(it) } }).toBe("""["",""]""")
		assert(write { writeListByElement(arrayOf("", "").asSequence()) { writeValue(it) } }).toBe("""["",""]""")
	}


	@Test
	fun testWriteListStartAndEnd() {
		assert(write {
			writeListStart()
			writeListEnd()
		}).toBe("[]")

		assert(write {
			writeListStart()
			writeInt(0)
			writeListEnd()
		}).toBe("[0]")

		assert(write {
			writeListStart()
			writeInt(0)
			writeInt(1); writeListEnd()
		}).toBe("[0,1]")
	}


	@Test
	fun testWriteListOrNull() {
		assert(write { writeListOrNull(booleanArrayOf(false, true)) }).toBe("[false,true]")
		assert(write { writeListOrNull(byteArrayOf(0, 1)) }).toBe("[0,1]")
		assert(write { writeListOrNull(charArrayOf(0.toChar(), 'a')) }).toBe("""["\u0000","a"]""")
		assert(write { writeListOrNull(doubleArrayOf(0.0, 1.0)) }).toBe("[0.0,1.0]")
		assert(write { writeListOrNull(floatArrayOf(0.0f, 1.0f)) }).toBe("[0.0,1.0]")
		assert(write { writeListOrNull(intArrayOf(0, 1)) }).toBe("[0,1]")
		assert(write { writeListOrNull(longArrayOf(0, 1)) }).toBe("[0,1]")
		assert(write { writeListOrNull(shortArrayOf(0, 1)) }).toBe("[0,1]")
		assert(write { writeListOrNull(arrayOf("", "")) }).toBe("""["",""]""")
		assert(write { writeListOrNull(arrayOf("", "").asSequence().asIterable()) }).toBe("""["",""]""")
		assert(write { writeListOrNull(arrayOf("", "").asSequence()) }).toBe("""["",""]""")
		assert(write { writeListOrNull(null as BooleanArray?) }).toBe("null")
		assert(write { writeListOrNull(null as ByteArray?) }).toBe("null")
		assert(write { writeListOrNull(null as CharArray?) }).toBe("null")
		assert(write { writeListOrNull(null as DoubleArray?) }).toBe("null")
		assert(write { writeListOrNull(null as FloatArray?) }).toBe("null")
		assert(write { writeListOrNull(null as IntArray?) }).toBe("null")
		assert(write { writeListOrNull(null as LongArray?) }).toBe("null")
		assert(write { writeListOrNull(null as ShortArray?) }).toBe("null")
		assert(write { writeListOrNull(null as Array<String?>?) }).toBe("null")
		assert(write { writeListOrNull(null as Iterable<String?>?) }).toBe("null")
		assert(write { writeListOrNull(null as Sequence<String?>?) }).toBe("null")
	}


	@Test
	fun testWriteListOrNullByElement() {
		assert(write { writeListOrNullByElement(booleanArrayOf(false, true)) { writeValue(it) } }).toBe("[false,true]")
		assert(write { writeListOrNullByElement(byteArrayOf(0, 1)) { writeValue(it) } }).toBe("[0,1]")
		assert(write { writeListOrNullByElement(charArrayOf(0.toChar(), 'a')) { writeValue(it) } }).toBe("""["\u0000","a"]""")
		assert(write { writeListOrNullByElement(doubleArrayOf(0.0, 1.0)) { writeValue(it) } }).toBe("[0.0,1.0]")
		assert(write { writeListOrNullByElement(floatArrayOf(0.0f, 1.0f)) { writeValue(it) } }).toBe("[0.0,1.0]")
		assert(write { writeListOrNullByElement(intArrayOf(0, 1)) { writeValue(it) } }).toBe("[0,1]")
		assert(write { writeListOrNullByElement(longArrayOf(0, 1)) { writeValue(it) } }).toBe("[0,1]")
		assert(write { writeListOrNullByElement(shortArrayOf(0, 1)) { writeValue(it) } }).toBe("[0,1]")
		assert(write { writeListOrNullByElement(arrayOf("", "")) { writeValue(it) } }).toBe("""["",""]""")
		assert(write { writeListOrNullByElement(arrayOf("", "").asSequence().asIterable()) { writeValue(it) } }).toBe("""["",""]""")
		assert(write { writeListOrNullByElement(arrayOf("", "").asSequence()) { writeValue(it) } }).toBe("""["",""]""")
		assert(write { writeListOrNullByElement(null as BooleanArray?) {} }).toBe("null")
		assert(write { writeListOrNullByElement(null as ByteArray?) {} }).toBe("null")
		assert(write { writeListOrNullByElement(null as CharArray?) {} }).toBe("null")
		assert(write { writeListOrNullByElement(null as DoubleArray?) {} }).toBe("null")
		assert(write { writeListOrNullByElement(null as FloatArray?) {} }).toBe("null")
		assert(write { writeListOrNullByElement(null as IntArray?) {} }).toBe("null")
		assert(write { writeListOrNullByElement(null as LongArray?) {} }).toBe("null")
		assert(write { writeListOrNullByElement(null as ShortArray?) {} }).toBe("null")
		assert(write { writeListOrNullByElement(null as Array<String?>?) {} }).toBe("null")
		assert(write { writeListOrNullByElement(null as Iterable<String?>?) {} }).toBe("null")
		assert(write { writeListOrNullByElement(null as Sequence<String?>?) {} }).toBe("null")
	}


	@Test
	fun testWriteLong() {
		assert(write { writeLong(Long.MIN_VALUE) }).toBe("-9223372036854775808")
		assert(write { writeLong(-1) }).toBe("-1")
		assert(write { writeLong(0) }).toBe("0")
		assert(write { writeLong(1) }).toBe("1")
		assert(write { writeLong(Long.MAX_VALUE) }).toBe("9223372036854775807")
	}


	@Test
	fun testWriteLongOrNull() {
		assert(write { writeLongOrNull(Long.MIN_VALUE) }).toBe("-9223372036854775808")
		assert(write { writeLongOrNull(-1) }).toBe("-1")
		assert(write { writeLongOrNull(0) }).toBe("0")
		assert(write { writeLongOrNull(1) }).toBe("1")
		assert(write { writeLongOrNull(Long.MAX_VALUE) }).toBe("9223372036854775807")
		assert(write { writeLongOrNull(null) }).toBe("null")
	}


	@Test
	fun testWriteMap() {
		assert(write { writeMap(mapOf("0" to 0, "1" to 1)) }).toBe("""{"0":0,"1":1}""")
	}


	@Test
	fun testWriteMapByElement() {
		assert(write {
			writeMapByElement(mapOf("0" to 0, "1" to 1)) { key, value ->
				writeMapKey(key)
				writeInt(value)
			}
		}).toBe("""{"0":0,"1":1}""")
	}


	@Test
	fun testWriteMapByElementValue() {
		assert(write {
			writeMapByElementValue(mapOf("0" to 0, "1" to 1)) { value ->
				writeInt(value)
			}
		}).toBe("""{"0":0,"1":1}""")
	}


	@Test
	fun testWriteMapElement() {
		assert(write {
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

				writeMapElement("", list = arrayOf(""))
				writeMapElement("", list = arrayOf(""), skipIfNull = false)
				writeMapElement("", list = arrayOf(""), skipIfNull = true)
				writeMapElement("", list = null as Array<*>?, skipIfNull = false)
				writeMapElement("", list = null as Array<*>?, skipIfNull = true)

				writeMapElement("", list = booleanArrayOf(true))
				writeMapElement("", list = booleanArrayOf(true), skipIfNull = false)
				writeMapElement("", list = booleanArrayOf(true), skipIfNull = true)
				writeMapElement("", list = null as BooleanArray?, skipIfNull = false)
				writeMapElement("", list = null as BooleanArray?, skipIfNull = true)

				writeMapElement("", list = byteArrayOf(0))
				writeMapElement("", list = byteArrayOf(0), skipIfNull = false)
				writeMapElement("", list = byteArrayOf(0), skipIfNull = true)
				writeMapElement("", list = null as ByteArray?, skipIfNull = false)
				writeMapElement("", list = null as ByteArray?, skipIfNull = true)

				writeMapElement("", list = charArrayOf(0.toChar()))
				writeMapElement("", list = charArrayOf(0.toChar()), skipIfNull = false)
				writeMapElement("", list = charArrayOf(0.toChar()), skipIfNull = true)
				writeMapElement("", list = null as CharArray?, skipIfNull = false)
				writeMapElement("", list = null as CharArray?, skipIfNull = true)

				writeMapElement("", list = doubleArrayOf(0.0))
				writeMapElement("", list = doubleArrayOf(0.0), skipIfNull = false)
				writeMapElement("", list = doubleArrayOf(0.0), skipIfNull = true)
				writeMapElement("", list = null as DoubleArray?, skipIfNull = false)
				writeMapElement("", list = null as DoubleArray?, skipIfNull = true)

				writeMapElement("", list = floatArrayOf(0.0f))
				writeMapElement("", list = floatArrayOf(0.0f), skipIfNull = false)
				writeMapElement("", list = floatArrayOf(0.0f), skipIfNull = true)
				writeMapElement("", list = null as FloatArray?, skipIfNull = false)
				writeMapElement("", list = null as FloatArray?, skipIfNull = true)

				writeMapElement("", list = intArrayOf(0))
				writeMapElement("", list = intArrayOf(0), skipIfNull = false)
				writeMapElement("", list = intArrayOf(0), skipIfNull = true)
				writeMapElement("", list = null as IntArray?, skipIfNull = false)
				writeMapElement("", list = null as IntArray?, skipIfNull = true)

				writeMapElement("", list = listOf("") as Iterable<*>)
				writeMapElement("", list = listOf("") as Iterable<*>, skipIfNull = false)
				writeMapElement("", list = listOf("") as Iterable<*>, skipIfNull = true)
				writeMapElement("", list = null as Iterable<*>?, skipIfNull = false)
				writeMapElement("", list = null as Iterable<*>?, skipIfNull = true)

				writeMapElement("", list = listOf(0) as Iterable<Int>) { writeInt(it + 1) }
				writeMapElement("", list = listOf(0) as Iterable<Int>, skipIfNull = false) { writeInt(it + 1) }
				writeMapElement("", list = listOf(0) as Iterable<Int>, skipIfNull = true) { writeInt(it + 1) }
				writeMapElement("", list = null as Iterable<Int>?, skipIfNull = false) { writeInt(it + 1) }
				writeMapElement("", list = null as Iterable<Int>?, skipIfNull = true) { writeInt(it + 1) }

				writeMapElement("", list = longArrayOf(0))
				writeMapElement("", list = longArrayOf(0), skipIfNull = false)
				writeMapElement("", list = longArrayOf(0), skipIfNull = true)
				writeMapElement("", list = null as LongArray?, skipIfNull = false)
				writeMapElement("", list = null as LongArray?, skipIfNull = true)

				writeMapElement("", list = listOf("").asSequence())
				writeMapElement("", list = listOf("").asSequence(), skipIfNull = false)
				writeMapElement("", list = listOf("").asSequence(), skipIfNull = true)
				writeMapElement("", list = null as Sequence<*>?, skipIfNull = false)
				writeMapElement("", list = null as Sequence<*>?, skipIfNull = true)

				writeMapElement("", list = listOf(0).asSequence()) { writeInt(it + 1) }
				writeMapElement("", list = listOf(0).asSequence(), skipIfNull = false) { writeInt(it + 1) }
				writeMapElement("", list = listOf(0).asSequence(), skipIfNull = true) { writeInt(it + 1) }
				writeMapElement("", list = null as Sequence<Int>?, skipIfNull = false) { writeInt(it + 1) }
				writeMapElement("", list = null as Sequence<Int>?, skipIfNull = true) { writeInt(it + 1) }

				writeMapElement("", list = shortArrayOf(0))
				writeMapElement("", list = shortArrayOf(0), skipIfNull = false)
				writeMapElement("", list = shortArrayOf(0), skipIfNull = true)
				writeMapElement("", list = null as ShortArray?, skipIfNull = false)
				writeMapElement("", list = null as ShortArray?, skipIfNull = true)

				writeMapElement("", long = 0)
				writeMapElement("", long = 0, skipIfNull = false)
				writeMapElement("", long = 0, skipIfNull = true)
				writeMapElement("", long = null, skipIfNull = false)
				writeMapElement("", long = null, skipIfNull = true)

				writeMapElement("", map = mapOf("" to 0) as Map<*, *>)
				writeMapElement("", map = mapOf("" to 0) as Map<*, *>, skipIfNull = false)
				writeMapElement("", map = mapOf("" to 0) as Map<*, *>, skipIfNull = true)
				writeMapElement("", map = null as Map<*, *>?, skipIfNull = false)
				writeMapElement("", map = null as Map<*, *>?, skipIfNull = true)

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
		assert(write {
			writeMapStart()
			writeMapEnd()
		}).toBe("{}")

		assert(write {
			writeMapStart()
			writeMapKey("0")
			writeInt(0)
			writeMapEnd()
		}).toBe("""{"0":0}""")

		assert(write {
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
		assert(write {
			writeMapStart()
			writeMapKey("")
			writeInt(0)
			writeMapEnd()
		}).toBe("""{"":0}""")

		assert(write {
			writeMapStart()
			writeMapKey("simple")
			writeInt(0)
			writeMapEnd()
		}).toBe("""{"simple":0}""")

		assert(write {
			writeMapStart()
			writeMapKey("emoji: 🐶")
			writeInt(0)
			writeMapEnd()
		}).toBe("""{"emoji: 🐶":0}""")

		assert(write {
			writeMapStart()
			writeMapKey("\\ \"")
			writeInt(0)
			writeMapEnd()
		}).toBe("""{"\\ \"":0}""")

		assert(write {
			writeMapStart()
			writeMapKey("\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\u000B\u000E\u000F\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001A\u001B\u001C\u001D\u001E\u001F\u0020")
			writeInt(0)
			writeMapEnd()
		}).toBe("""{"\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\u000B\u000E\u000F\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001A\u001B\u001C\u001D\u001E\u001F ":0}""")

		assert(write {
			writeMapStart()
			writeMapKey(" \\ \" / \b \u000C \n \r \t ")
			writeInt(0)
			writeMapEnd()
		}).toBe("""{" \\ \" / \b \f \n \r \t ":0}""")
	}


	@Test
	fun testWriteMapOrNull() {
		assert(write { writeMapOrNull(mapOf("0" to 0, "1" to 1)) }).toBe("""{"0":0,"1":1}""")
		assert(write { writeMapOrNull(null) }).toBe("null")
	}


	@Test
	fun testWriteMapOrNullByElement() {
		assert(write {
			writeMapOrNullByElement(mapOf("0" to 0, "1" to 1)) { key, value ->
				writeMapKey(key)
				writeInt(value)
			}
		}).toBe("""{"0":0,"1":1}""")

		assert(write {
			writeMapOrNullByElement(null as Map<String, Int>?) { _, _ -> Unit }
		}).toBe("null")
	}


	@Test
	fun testWriteMapOrNullByElementValue() {
		assert(write {
			writeMapOrNullByElementValue(mapOf("0" to 0, "1" to 1)) { value ->
				writeInt(value)
			}
		}).toBe("""{"0":0,"1":1}""")

		assert(write {
			writeMapOrNullByElementValue(null as Map<String, Int>?) {}
		}).toBe("null")
	}


	@Test
	fun testWriteNull() {
		assert(write { writeNull() }).toBe("null")
	}


	@Test
	fun testWriteNumber() {
		assert(write { writeNumber(Long.MIN_VALUE) }).toBe("-9223372036854775808")
		assert(write { writeNumber(Int.MIN_VALUE - 1L) }).toBe("-2147483649")
		assert(write { writeNumber(Int.MIN_VALUE) }).toBe("-2147483648")
		assert(write { writeNumber(-1) }).toBe("-1")
		assert(write { writeNumber(0) }).toBe("0")
		assert(write { writeNumber(1) }).toBe("1")
		assert(write { writeNumber(Int.MAX_VALUE) }).toBe("2147483647")
		assert(write { writeNumber(Int.MAX_VALUE + 1L) }).toBe("2147483648")
		assert(write { writeNumber(Long.MAX_VALUE) }).toBe("9223372036854775807")
		assert(write { writeNumber(-1E200) }).toBe("-1.0E200")
		assert(write { writeNumber(-100.999) }).toBe("-100.999")
		assert(write { writeNumber(-100.001) }).toBe("-100.001")
		assert(write { writeNumber(-1E-200) }).toBe("-1.0E-200")
		assert(write { writeNumber(-0.0) }).toBe("-0.0")
		assert(write { writeNumber(0.0) }).toBe("0.0")
		assert(write { writeNumber(1E-200) }).toBe("1.0E-200")
		assert(write { writeNumber(100.001) }).toBe("100.001")
		assert(write { writeNumber(100.999) }).toBe("100.999")
		assert(write { writeNumber(1E200) }).toBe("1.0E200")
	}


	@Test
	fun testWriteNumberOrNull() {
		assert(write { writeNumberOrNull(Long.MIN_VALUE) }).toBe("-9223372036854775808")
		assert(write { writeNumberOrNull(Int.MIN_VALUE - 1L) }).toBe("-2147483649")
		assert(write { writeNumberOrNull(Int.MIN_VALUE) }).toBe("-2147483648")
		assert(write { writeNumberOrNull(-1) }).toBe("-1")
		assert(write { writeNumberOrNull(0) }).toBe("0")
		assert(write { writeNumberOrNull(1) }).toBe("1")
		assert(write { writeNumberOrNull(Int.MAX_VALUE) }).toBe("2147483647")
		assert(write { writeNumberOrNull(Int.MAX_VALUE + 1L) }).toBe("2147483648")
		assert(write { writeNumberOrNull(Long.MAX_VALUE) }).toBe("9223372036854775807")
		assert(write { writeNumberOrNull(-1E200) }).toBe("-1.0E200")
		assert(write { writeNumberOrNull(-100.999) }).toBe("-100.999")
		assert(write { writeNumberOrNull(-100.001) }).toBe("-100.001")
		assert(write { writeNumberOrNull(-1E-200) }).toBe("-1.0E-200")
		assert(write { writeNumberOrNull(-0.0) }).toBe("-0.0")
		assert(write { writeNumberOrNull(0.0) }).toBe("0.0")
		assert(write { writeNumberOrNull(1E-200) }).toBe("1.0E-200")
		assert(write { writeNumberOrNull(100.001) }).toBe("100.001")
		assert(write { writeNumberOrNull(100.999) }).toBe("100.999")
		assert(write { writeNumberOrNull(1E200) }).toBe("1.0E200")
		assert(write { writeNumberOrNull(null) }).toBe("null")
	}


	@Test
	fun testWriteShort() {
		assert(write { writeShort(Short.MIN_VALUE) }).toBe("-32768")
		assert(write { writeShort(-1) }).toBe("-1")
		assert(write { writeShort(0) }).toBe("0")
		assert(write { writeShort(1) }).toBe("1")
		assert(write { writeShort(Short.MAX_VALUE) }).toBe("32767")
	}


	@Test
	fun testWriteShortOrNull() {
		assert(write { writeShortOrNull(Short.MIN_VALUE) }).toBe("-32768")
		assert(write { writeShortOrNull(-1) }).toBe("-1")
		assert(write { writeShortOrNull(0) }).toBe("0")
		assert(write { writeShortOrNull(1) }).toBe("1")
		assert(write { writeShortOrNull(Short.MAX_VALUE) }).toBe("32767")
		assert(write { writeShortOrNull(null) }).toBe("null")
	}


	@Test
	fun testWriteString() {
		assert(write { writeString("") }).toBe("\"\"")
		assert(write { writeString("simple") }).toBe("\"simple\"")
		assert(write { writeString("emoji: 🐶") }).toBe("\"emoji: 🐶\"")
		assert(write { writeString("\\ \"") }).toBe("\"\\\\ \\\"\"")
		assert(write { writeString("\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\u000B\u000E\u000F\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001A\u001B\u001C\u001D\u001E\u001F\u0020") }).toBe("\"\\u0000\\u0001\\u0002\\u0003\\u0004\\u0005\\u0006\\u0007\\u000B\\u000E\\u000F\\u0010\\u0011\\u0012\\u0013\\u0014\\u0015\\u0016\\u0017\\u0018\\u0019\\u001A\\u001B\\u001C\\u001D\\u001E\\u001F \"")
		assert(write { writeString(" \\ \" / \b \u000C \n \r \t ") }).toBe("\" \\\\ \\\" / \\b \\f \\n \\r \\t \"")
	}


	@Test
	fun testWriteStringOrNull() {
		assert(write { writeStringOrNull("") }).toBe("\"\"")
		assert(write { writeStringOrNull("simple") }).toBe("\"simple\"")
		assert(write { writeStringOrNull("emoji: 🐶") }).toBe("\"emoji: 🐶\"")
		assert(write { writeStringOrNull("\\ \"") }).toBe("\"\\\\ \\\"\"")
		assert(write { writeStringOrNull("\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\u000B\u000E\u000F\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001A\u001B\u001C\u001D\u001E\u001F\u0020") }).toBe("\"\\u0000\\u0001\\u0002\\u0003\\u0004\\u0005\\u0006\\u0007\\u000B\\u000E\\u000F\\u0010\\u0011\\u0012\\u0013\\u0014\\u0015\\u0016\\u0017\\u0018\\u0019\\u001A\\u001B\\u001C\\u001D\\u001E\\u001F \"")
		assert(write { writeStringOrNull(" \\ \" / \b \u000C \n \r \t ") }).toBe("\" \\\\ \\\" / \\b \\f \\n \\r \\t \"")
		assert(write { writeStringOrNull(null) }).toBe("null")
	}


	@Test
	fun testWriteValue() {
		assert(write {
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
		assert(write {
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
		assert(write {
			writeIntoMap {
				writeValue("hey")
				writeNull()
			}
		}).toBe("""{"hey":null}""")
	}


	private fun write(block: JSONWriter.() -> Unit): String =
		StringWriter().also { JSONWriter.build(it).block() }.toString()
}
