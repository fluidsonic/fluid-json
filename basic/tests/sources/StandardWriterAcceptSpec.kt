package tests.basic

import com.github.fluidsonic.fluid.json.*
import com.winterbe.expekt.should
import org.spekframework.spek2.Spek
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.style.specification.describe
import java.io.StringWriter


internal object StandardWriterAcceptSpec : Spek({

	describe("StandardWriter succeeds for") {

		it(".close()") {
			write { writeNull(); close() }
			write { writeNull(); close(); close() }
		}

		it("terminate()") {
			write { writeNull(); terminate() }
		}

		it(".markAsErrored()") {
			write {
				isErrored.should.be.`false`
				markAsErrored()
				isErrored.should.be.`true`
			}
		}

		it(".writeBoolean()") {
			write { writeBoolean(false) }.should.equal("false")
			write { writeBoolean(true) }.should.equal("true")
		}

		it(".writeBooleanOrNull()") {
			write { writeBooleanOrNull(false) }.should.equal("false")
			write { writeBooleanOrNull(true) }.should.equal("true")
			write { writeBooleanOrNull(null) }.should.equal("null")
		}

		it(".writeByte()") {
			write { writeByte(Byte.MIN_VALUE) }.should.equal("-128")
			write { writeByte(-1) }.should.equal("-1")
			write { writeByte(0) }.should.equal("0")
			write { writeByte(1) }.should.equal("1")
			write { writeByte(Byte.MAX_VALUE) }.should.equal("127")
		}

		it(".writeByteOrNull()") {
			write { writeByteOrNull(Byte.MIN_VALUE) }.should.equal("-128")
			write { writeByteOrNull(-1) }.should.equal("-1")
			write { writeByteOrNull(0) }.should.equal("0")
			write { writeByteOrNull(1) }.should.equal("1")
			write { writeByteOrNull(Byte.MAX_VALUE) }.should.equal("127")
			write { writeByteOrNull(null) }.should.equal("null")
		}

		it(".writeChar()") {
			write { writeChar(Char.MIN_VALUE) }.should.equal(""""\u0000"""")
			write { writeChar('a') }.should.equal(""""a"""")
			write { writeChar(Char.MAX_VALUE) }.should.equal("\"\uFFFF\"")
		}

		it(".writeCharOrNull()") {
			write { writeCharOrNull(Char.MIN_VALUE) }.should.equal(""""\u0000"""")
			write { writeCharOrNull('a') }.should.equal(""""a"""")
			write { writeCharOrNull(Char.MAX_VALUE) }.should.equal("\"\uFFFF\"")
			write { writeCharOrNull(null) }.should.equal("null")
		}

		it(".writeDouble()") {
			write { writeDouble(-1E200) }.should.equal("-1.0E200")
			write { writeDouble(-100.999) }.should.equal("-100.999")
			write { writeDouble(-100.001) }.should.equal("-100.001")
			write { writeDouble(-1E-200) }.should.equal("-1.0E-200")
			write { writeDouble(-0.0) }.should.equal("-0.0")
			write { writeDouble(0.0) }.should.equal("0.0")
			write { writeDouble(1E-200) }.should.equal("1.0E-200")
			write { writeDouble(100.001) }.should.equal("100.001")
			write { writeDouble(100.999) }.should.equal("100.999")
			write { writeDouble(1E200) }.should.equal("1.0E200")
		}

		it(".writeDoubleOrNull()") {
			write { writeDoubleOrNull(-1E200) }.should.equal("-1.0E200")
			write { writeDoubleOrNull(-100.999) }.should.equal("-100.999")
			write { writeDoubleOrNull(-100.001) }.should.equal("-100.001")
			write { writeDoubleOrNull(-1E-200) }.should.equal("-1.0E-200")
			write { writeDoubleOrNull(-0.0) }.should.equal("-0.0")
			write { writeDoubleOrNull(0.0) }.should.equal("0.0")
			write { writeDoubleOrNull(1E-200) }.should.equal("1.0E-200")
			write { writeDoubleOrNull(100.001) }.should.equal("100.001")
			write { writeDoubleOrNull(100.999) }.should.equal("100.999")
			write { writeDoubleOrNull(1E200) }.should.equal("1.0E200")
			write { writeDoubleOrNull(null) }.should.equal("null")
		}

		it(".writeFloat()") {
			write { writeFloat(-1E38f) }.should.equal("-1.0E38")
			write { writeFloat(-100.999f) }.should.equal("-100.999")
			write { writeFloat(-100.001f) }.should.equal("-100.001")
			write { writeFloat(-1E-38f) }.should.equal("-1.0E-38")
			write { writeFloat(-0.0f) }.should.equal("-0.0")
			write { writeFloat(0.0f) }.should.equal("0.0")
			write { writeFloat(1E-38f) }.should.equal("1.0E-38")
			write { writeFloat(100.001f) }.should.equal("100.001")
			write { writeFloat(100.999f) }.should.equal("100.999")
			write { writeFloat(1E38f) }.should.equal("1.0E38")
		}

		it(".writeFloatOrNull()") {
			write { writeFloatOrNull(-1E38f) }.should.equal("-1.0E38")
			write { writeFloatOrNull(-100.999f) }.should.equal("-100.999")
			write { writeFloatOrNull(-100.001f) }.should.equal("-100.001")
			write { writeFloatOrNull(-1E-38f) }.should.equal("-1.0E-38")
			write { writeFloatOrNull(-0.0f) }.should.equal("-0.0")
			write { writeFloatOrNull(0.0f) }.should.equal("0.0")
			write { writeFloatOrNull(1E-38f) }.should.equal("1.0E-38")
			write { writeFloatOrNull(100.001f) }.should.equal("100.001")
			write { writeFloatOrNull(100.999f) }.should.equal("100.999")
			write { writeFloatOrNull(1E38f) }.should.equal("1.0E38")
			write { writeFloatOrNull(null) }.should.equal("null")
		}

		it(".writeInt()") {
			write { writeInt(Int.MIN_VALUE) }.should.equal("-2147483648")
			write { writeInt(-1) }.should.equal("-1")
			write { writeInt(0) }.should.equal("0")
			write { writeInt(1) }.should.equal("1")
			write { writeInt(Int.MAX_VALUE) }.should.equal("2147483647")
		}

		it(".writeIntOrNull()") {
			write { writeIntOrNull(Int.MIN_VALUE) }.should.equal("-2147483648")
			write { writeIntOrNull(-1) }.should.equal("-1")
			write { writeIntOrNull(0) }.should.equal("0")
			write { writeIntOrNull(1) }.should.equal("1")
			write { writeIntOrNull(Int.MAX_VALUE) }.should.equal("2147483647")
			write { writeIntOrNull(null) }.should.equal("null")
		}

		it(".writeIntoList()") {
			write {
				writeIntoList {}
			}.should.equal("[]")

			write {
				writeIntoList {
					writeNull()
					writeInt(1)
				}
			}.should.equal("[null,1]")
		}

		it(".writeIntoMap()") {
			write {
				writeIntoMap {}
			}.should.equal("{}")

			write {
				writeIntoMap {
					writeMapKey("")
					writeInt(1)
				}
			}.should.equal("""{"":1}""")
		}

		it(".writeList()") {
			write { writeList(booleanArrayOf(false, true)) }.should.equal("[false,true]")
			write { writeList(byteArrayOf(0, 1)) }.should.equal("[0,1]")
			write { writeList(charArrayOf(0.toChar(), 'a')) }.should.equal("""["\u0000","a"]""")
			write { writeList(doubleArrayOf(0.0, 1.0)) }.should.equal("[0.0,1.0]")
			write { writeList(floatArrayOf(0.0f, 1.0f)) }.should.equal("[0.0,1.0]")
			write { writeList(intArrayOf(0, 1)) }.should.equal("[0,1]")
			write { writeList(longArrayOf(0, 1)) }.should.equal("[0,1]")
			write { writeList(shortArrayOf(0, 1)) }.should.equal("[0,1]")
			write { writeList(arrayOf("", "")) }.should.equal("""["",""]""")
			write { writeList(arrayOf("", "").asSequence().asIterable()) }.should.equal("""["",""]""")
			write { writeList(arrayOf("", "").asSequence()) }.should.equal("""["",""]""")
		}

		it(".writeListByElement()") {
			write { writeListByElement(booleanArrayOf(false, true)) { writeValue(it) } }.should.equal("[false,true]")
			write { writeListByElement(byteArrayOf(0, 1)) { writeValue(it) } }.should.equal("[0,1]")
			write { writeListByElement(charArrayOf(0.toChar(), 'a')) { writeValue(it) } }.should.equal("""["\u0000","a"]""")
			write { writeListByElement(doubleArrayOf(0.0, 1.0)) { writeValue(it) } }.should.equal("[0.0,1.0]")
			write { writeListByElement(floatArrayOf(0.0f, 1.0f)) { writeValue(it) } }.should.equal("[0.0,1.0]")
			write { writeListByElement(intArrayOf(0, 1)) { writeValue(it) } }.should.equal("[0,1]")
			write { writeListByElement(longArrayOf(0, 1)) { writeValue(it) } }.should.equal("[0,1]")
			write { writeListByElement(shortArrayOf(0, 1)) { writeValue(it) } }.should.equal("[0,1]")
			write { writeListByElement(arrayOf("", "")) { writeValue(it) } }.should.equal("""["",""]""")
			write { writeListByElement(arrayOf("", "").asSequence().asIterable()) { writeValue(it) } }.should.equal("""["",""]""")
			write { writeListByElement(arrayOf("", "").asSequence()) { writeValue(it) } }.should.equal("""["",""]""")
		}

		it(".writeListEnd() / .writeListStart()") {
			write {
				writeListStart()
				writeListEnd()
			}.should.equal("[]")

			write {
				writeListStart()
				writeInt(0)
				writeListEnd()
			}.should.equal("[0]")

			write {
				writeListStart()
				writeInt(0)
				writeInt(1); writeListEnd()
			}.should.equal("[0,1]")
		}

		it(".writeListOrNull()") {
			write { writeListOrNull(booleanArrayOf(false, true)) }.should.equal("[false,true]")
			write { writeListOrNull(byteArrayOf(0, 1)) }.should.equal("[0,1]")
			write { writeListOrNull(charArrayOf(0.toChar(), 'a')) }.should.equal("""["\u0000","a"]""")
			write { writeListOrNull(doubleArrayOf(0.0, 1.0)) }.should.equal("[0.0,1.0]")
			write { writeListOrNull(floatArrayOf(0.0f, 1.0f)) }.should.equal("[0.0,1.0]")
			write { writeListOrNull(intArrayOf(0, 1)) }.should.equal("[0,1]")
			write { writeListOrNull(longArrayOf(0, 1)) }.should.equal("[0,1]")
			write { writeListOrNull(shortArrayOf(0, 1)) }.should.equal("[0,1]")
			write { writeListOrNull(arrayOf("", "")) }.should.equal("""["",""]""")
			write { writeListOrNull(arrayOf("", "").asSequence().asIterable()) }.should.equal("""["",""]""")
			write { writeListOrNull(arrayOf("", "").asSequence()) }.should.equal("""["",""]""")
			write { writeListOrNull(null as BooleanArray?) }.should.equal("null")
			write { writeListOrNull(null as ByteArray?) }.should.equal("null")
			write { writeListOrNull(null as CharArray?) }.should.equal("null")
			write { writeListOrNull(null as DoubleArray?) }.should.equal("null")
			write { writeListOrNull(null as FloatArray?) }.should.equal("null")
			write { writeListOrNull(null as IntArray?) }.should.equal("null")
			write { writeListOrNull(null as LongArray?) }.should.equal("null")
			write { writeListOrNull(null as ShortArray?) }.should.equal("null")
			write { writeListOrNull(null as Array<String?>?) }.should.equal("null")
			write { writeListOrNull(null as Iterable<String?>?) }.should.equal("null")
			write { writeListOrNull(null as Sequence<String?>?) }.should.equal("null")
		}

		it(".writeListOrNullByElement()") {
			write { writeListOrNullByElement(booleanArrayOf(false, true)) { writeValue(it) } }.should.equal("[false,true]")
			write { writeListOrNullByElement(byteArrayOf(0, 1)) { writeValue(it) } }.should.equal("[0,1]")
			write { writeListOrNullByElement(charArrayOf(0.toChar(), 'a')) { writeValue(it) } }.should.equal("""["\u0000","a"]""")
			write { writeListOrNullByElement(doubleArrayOf(0.0, 1.0)) { writeValue(it) } }.should.equal("[0.0,1.0]")
			write { writeListOrNullByElement(floatArrayOf(0.0f, 1.0f)) { writeValue(it) } }.should.equal("[0.0,1.0]")
			write { writeListOrNullByElement(intArrayOf(0, 1)) { writeValue(it) } }.should.equal("[0,1]")
			write { writeListOrNullByElement(longArrayOf(0, 1)) { writeValue(it) } }.should.equal("[0,1]")
			write { writeListOrNullByElement(shortArrayOf(0, 1)) { writeValue(it) } }.should.equal("[0,1]")
			write { writeListOrNullByElement(arrayOf("", "")) { writeValue(it) } }.should.equal("""["",""]""")
			write { writeListOrNullByElement(arrayOf("", "").asSequence().asIterable()) { writeValue(it) } }.should.equal("""["",""]""")
			write { writeListOrNullByElement(arrayOf("", "").asSequence()) { writeValue(it) } }.should.equal("""["",""]""")
			write { writeListOrNullByElement(null as BooleanArray?) {} }.should.equal("null")
			write { writeListOrNullByElement(null as ByteArray?) {} }.should.equal("null")
			write { writeListOrNullByElement(null as CharArray?) {} }.should.equal("null")
			write { writeListOrNullByElement(null as DoubleArray?) {} }.should.equal("null")
			write { writeListOrNullByElement(null as FloatArray?) {} }.should.equal("null")
			write { writeListOrNullByElement(null as IntArray?) {} }.should.equal("null")
			write { writeListOrNullByElement(null as LongArray?) {} }.should.equal("null")
			write { writeListOrNullByElement(null as ShortArray?) {} }.should.equal("null")
			write { writeListOrNullByElement(null as Array<String?>?) {} }.should.equal("null")
			write { writeListOrNullByElement(null as Iterable<String?>?) {} }.should.equal("null")
			write { writeListOrNullByElement(null as Sequence<String?>?) {} }.should.equal("null")
		}

		it(".writeLong()") {
			write { writeLong(Long.MIN_VALUE) }.should.equal("-9223372036854775808")
			write { writeLong(-1) }.should.equal("-1")
			write { writeLong(0) }.should.equal("0")
			write { writeLong(1) }.should.equal("1")
			write { writeLong(Long.MAX_VALUE) }.should.equal("9223372036854775807")
		}

		it(".writeLongOrNull()") {
			write { writeLongOrNull(Long.MIN_VALUE) }.should.equal("-9223372036854775808")
			write { writeLongOrNull(-1) }.should.equal("-1")
			write { writeLongOrNull(0) }.should.equal("0")
			write { writeLongOrNull(1) }.should.equal("1")
			write { writeLongOrNull(Long.MAX_VALUE) }.should.equal("9223372036854775807")
			write { writeLongOrNull(null) }.should.equal("null")
		}

		it(".writeMap()") {
			write { writeMap(mapOf("0" to 0, "1" to 1)) }.should.equal("""{"0":0,"1":1}""")
		}

		it(".writeMapByElement()") {
			write {
				writeMapByElement(mapOf("0" to 0, "1" to 1)) { key, value ->
					writeMapKey(key)
					writeInt(value)
				}
			}.should.equal("""{"0":0,"1":1}""")
		}

		it(".writeMapByElementValue()") {
			write {
				writeMapByElementValue(mapOf("0" to 0, "1" to 1)) { value ->
					writeInt(value)
				}
			}.should.equal("""{"0":0,"1":1}""")
		}

		it(".writeMapElement()") {
			write {
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
			}.should.equal("""
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

		it(".writeMapEnd() / .writeMapStart()") {
			write {
				writeMapStart()
				writeMapEnd()
			}.should.equal("{}")

			write {
				writeMapStart()
				writeMapKey("0")
				writeInt(0)
				writeMapEnd()
			}.should.equal("""{"0":0}""")

			write {
				writeMapStart()
				writeMapKey("0")
				writeInt(0)
				writeMapKey("1")
				writeInt(1)
				writeMapEnd()
			}.should.equal("""{"0":0,"1":1}""")
		}

		it(".writeMapKey()") {
			write {
				writeMapStart()
				writeMapKey("")
				writeInt(0)
				writeMapEnd()
			}.should.equal("""{"":0}""")

			write {
				writeMapStart()
				writeMapKey("simple")
				writeInt(0)
				writeMapEnd()
			}.should.equal("""{"simple":0}""")

			write {
				writeMapStart()
				writeMapKey("emoji: 🐶")
				writeInt(0)
				writeMapEnd()
			}.should.equal("""{"emoji: 🐶":0}""")

			write {
				writeMapStart()
				writeMapKey("\\ \"")
				writeInt(0)
				writeMapEnd()
			}.should.equal("""{"\\ \"":0}""")

			write {
				writeMapStart()
				writeMapKey("\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\u000B\u000E\u000F\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001A\u001B\u001C\u001D\u001E\u001F\u0020")
				writeInt(0)
				writeMapEnd()
			}.should.equal("""{"\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\u000B\u000E\u000F\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001A\u001B\u001C\u001D\u001E\u001F ":0}""")

			write {
				writeMapStart()
				writeMapKey(" \\ \" / \b \u000C \n \r \t ")
				writeInt(0)
				writeMapEnd()
			}.should.equal("""{" \\ \" / \b \f \n \r \t ":0}""")
		}

		it(".writeMapOrNull()") {
			write { writeMapOrNull(mapOf("0" to 0, "1" to 1)) }.should.equal("""{"0":0,"1":1}""")
			write { writeMapOrNull(null) }.should.equal("null")
		}

		it(".writeMapOrNullByElement()") {
			write {
				writeMapOrNullByElement(mapOf("0" to 0, "1" to 1)) { key, value ->
					writeMapKey(key)
					writeInt(value)
				}
			}.should.equal("""{"0":0,"1":1}""")

			write {
				writeMapOrNullByElement(null as Map<String, Int>?) { _, _ -> Unit }
			}.should.equal("null")
		}

		it(".writeMapOrNullByElementValue()") {
			write {
				writeMapOrNullByElementValue(mapOf("0" to 0, "1" to 1)) { value ->
					writeInt(value)
				}
			}.should.equal("""{"0":0,"1":1}""")

			write {
				writeMapOrNullByElementValue(null as Map<String, Int>?) {}
			}.should.equal("null")
		}

		it(".writeNull()") {
			write { writeNull() }.should.equal("null")
		}

		it(".writeNumber()") {
			write { writeNumber(Long.MIN_VALUE) }.should.equal("-9223372036854775808")
			write { writeNumber(Int.MIN_VALUE - 1L) }.should.equal("-2147483649")
			write { writeNumber(Int.MIN_VALUE) }.should.equal("-2147483648")
			write { writeNumber(-1) }.should.equal("-1")
			write { writeNumber(0) }.should.equal("0")
			write { writeNumber(1) }.should.equal("1")
			write { writeNumber(Int.MAX_VALUE) }.should.equal("2147483647")
			write { writeNumber(Int.MAX_VALUE + 1L) }.should.equal("2147483648")
			write { writeNumber(Long.MAX_VALUE) }.should.equal("9223372036854775807")
			write { writeNumber(-1E200) }.should.equal("-1.0E200")
			write { writeNumber(-100.999) }.should.equal("-100.999")
			write { writeNumber(-100.001) }.should.equal("-100.001")
			write { writeNumber(-1E-200) }.should.equal("-1.0E-200")
			write { writeNumber(-0.0) }.should.equal("-0.0")
			write { writeNumber(0.0) }.should.equal("0.0")
			write { writeNumber(1E-200) }.should.equal("1.0E-200")
			write { writeNumber(100.001) }.should.equal("100.001")
			write { writeNumber(100.999) }.should.equal("100.999")
			write { writeNumber(1E200) }.should.equal("1.0E200")
		}

		it(".writeNumberOrNull()") {
			write { writeNumberOrNull(Long.MIN_VALUE) }.should.equal("-9223372036854775808")
			write { writeNumberOrNull(Int.MIN_VALUE - 1L) }.should.equal("-2147483649")
			write { writeNumberOrNull(Int.MIN_VALUE) }.should.equal("-2147483648")
			write { writeNumberOrNull(-1) }.should.equal("-1")
			write { writeNumberOrNull(0) }.should.equal("0")
			write { writeNumberOrNull(1) }.should.equal("1")
			write { writeNumberOrNull(Int.MAX_VALUE) }.should.equal("2147483647")
			write { writeNumberOrNull(Int.MAX_VALUE + 1L) }.should.equal("2147483648")
			write { writeNumberOrNull(Long.MAX_VALUE) }.should.equal("9223372036854775807")
			write { writeNumberOrNull(-1E200) }.should.equal("-1.0E200")
			write { writeNumberOrNull(-100.999) }.should.equal("-100.999")
			write { writeNumberOrNull(-100.001) }.should.equal("-100.001")
			write { writeNumberOrNull(-1E-200) }.should.equal("-1.0E-200")
			write { writeNumberOrNull(-0.0) }.should.equal("-0.0")
			write { writeNumberOrNull(0.0) }.should.equal("0.0")
			write { writeNumberOrNull(1E-200) }.should.equal("1.0E-200")
			write { writeNumberOrNull(100.001) }.should.equal("100.001")
			write { writeNumberOrNull(100.999) }.should.equal("100.999")
			write { writeNumberOrNull(1E200) }.should.equal("1.0E200")
			write { writeNumberOrNull(null) }.should.equal("null")
		}

		it(".writeShort()") {
			write { writeShort(Short.MIN_VALUE) }.should.equal("-32768")
			write { writeShort(-1) }.should.equal("-1")
			write { writeShort(0) }.should.equal("0")
			write { writeShort(1) }.should.equal("1")
			write { writeShort(Short.MAX_VALUE) }.should.equal("32767")
		}

		it(".writeShortOrNull()") {
			write { writeShortOrNull(Short.MIN_VALUE) }.should.equal("-32768")
			write { writeShortOrNull(-1) }.should.equal("-1")
			write { writeShortOrNull(0) }.should.equal("0")
			write { writeShortOrNull(1) }.should.equal("1")
			write { writeShortOrNull(Short.MAX_VALUE) }.should.equal("32767")
			write { writeShortOrNull(null) }.should.equal("null")
		}

		it(".writeString()") {
			write { writeString("") }.should.equal("\"\"")
			write { writeString("simple") }.should.equal("\"simple\"")
			write { writeString("emoji: 🐶") }.should.equal("\"emoji: 🐶\"")
			write { writeString("\\ \"") }.should.equal("\"\\\\ \\\"\"")
			write { writeString("\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\u000B\u000E\u000F\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001A\u001B\u001C\u001D\u001E\u001F\u0020") }.should.equal("\"\\u0000\\u0001\\u0002\\u0003\\u0004\\u0005\\u0006\\u0007\\u000B\\u000E\\u000F\\u0010\\u0011\\u0012\\u0013\\u0014\\u0015\\u0016\\u0017\\u0018\\u0019\\u001A\\u001B\\u001C\\u001D\\u001E\\u001F \"")
			write { writeString(" \\ \" / \b \u000C \n \r \t ") }.should.equal("\" \\\\ \\\" / \\b \\f \\n \\r \\t \"")
		}

		it(".writeStringOrNull()") {
			write { writeStringOrNull("") }.should.equal("\"\"")
			write { writeStringOrNull("simple") }.should.equal("\"simple\"")
			write { writeStringOrNull("emoji: 🐶") }.should.equal("\"emoji: 🐶\"")
			write { writeStringOrNull("\\ \"") }.should.equal("\"\\\\ \\\"\"")
			write { writeStringOrNull("\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\u000B\u000E\u000F\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001A\u001B\u001C\u001D\u001E\u001F\u0020") }.should.equal("\"\\u0000\\u0001\\u0002\\u0003\\u0004\\u0005\\u0006\\u0007\\u000B\\u000E\\u000F\\u0010\\u0011\\u0012\\u0013\\u0014\\u0015\\u0016\\u0017\\u0018\\u0019\\u001A\\u001B\\u001C\\u001D\\u001E\\u001F \"")
			write { writeStringOrNull(" \\ \" / \b \u000C \n \r \t ") }.should.equal("\" \\\\ \\\" / \\b \\f \\n \\r \\t \"")
			write { writeStringOrNull(null) }.should.equal("null")
		}

		it(".writeValue()") {
			write {
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
			}.should.equal("""[["",""],true,[true,true],1,[0,1],"\u0000",["\u0000","a"],1.0,[0.0,1.0],1.0,[0.0,1.0],1,[0,1],[true,true],1,[0,1],{"0":0,"1":1},[true,true],1,[0,1],"",1.0]""")
		}

		it(".writeValueOrNull()") {
			write {
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
			}.should.equal("""[["",""],true,[true,true],1,[0,1],"\u0000",["\u0000","a"],1.0,[0.0,1.0],1.0,[0.0,1.0],1,[0,1],[true,true],1,[0,1],{"0":0,"1":1},[true,true],1,[0,1],"",1.0,null]""")
		}

		it(".writeValue() as map key") {
			write {
				writeIntoMap {
					writeValue("hey")
					writeNull()
				}
			}.should.equal("""{"hey":null}""")
		}
	}
})


// TODO move the following methods inside the object above once KT-19796 is fixed
// https://youtrack.jetbrains.com/issue/KT-19796

@Suppress("unused")
private fun TestBody.write(block: JSONWriter.() -> Unit): String =
	StringWriter().also { JSONWriter.build(it).block() }.toString()
