package tests

import com.github.fluidsonic.fluid.json.JSONReader
import com.github.fluidsonic.fluid.json.JSONToken
import com.github.fluidsonic.fluid.json.TextInput
import com.github.fluidsonic.fluid.json.TextInputReader
import com.github.fluidsonic.fluid.json.readList
import com.github.fluidsonic.fluid.json.readListByElement
import com.github.fluidsonic.fluid.json.readListOrNull
import com.github.fluidsonic.fluid.json.readMap
import com.github.fluidsonic.fluid.json.readMapByEntry
import com.github.fluidsonic.fluid.json.readMapOrNull
import com.github.fluidsonic.fluid.json.readValue
import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.io.StringReader


internal object TextInputReaderAcceptSpec : Spek({

	describe("TextInputReader succeeds for") {

		describe(".nextToken") {

			it("should match full tokens") {
				reader("null").nextToken.should.equal(JSONToken.nullValue)
				reader("true").nextToken.should.equal(JSONToken.booleanValue)
				reader("false").nextToken.should.equal(JSONToken.booleanValue)
				reader("0").nextToken.should.equal(JSONToken.numberValue)
				reader("1").nextToken.should.equal(JSONToken.numberValue)
				reader("-1.0").nextToken.should.equal(JSONToken.numberValue)
				reader("\"\"").nextToken.should.equal(JSONToken.stringValue)
				reader("[]").apply {
					nextToken.should.equal(JSONToken.listStart)
					readListStart()
					nextToken.should.equal(JSONToken.listEnd)
				}
				reader("{}").apply {
					nextToken.should.equal(JSONToken.mapStart)
					readMapStart()
					nextToken.should.equal(JSONToken.mapEnd)
				}
			}

			it("should consume minimal input") {
				reader("n").nextToken.should.equal(JSONToken.nullValue)
				reader("t").nextToken.should.equal(JSONToken.booleanValue)
				reader("f").nextToken.should.equal(JSONToken.booleanValue)
				reader("0").nextToken.should.equal(JSONToken.numberValue)
				reader("1").nextToken.should.equal(JSONToken.numberValue)
				reader("-").nextToken.should.equal(JSONToken.numberValue)
				reader("[").nextToken.should.equal(JSONToken.listStart)
				reader("\"").nextToken.should.equal(JSONToken.stringValue)
				reader("{").nextToken.should.equal(JSONToken.mapStart)
			}

			it("should be idempotent") {
				reader("n").apply { nextToken; nextToken.should.equal(JSONToken.nullValue) }
				reader("t").apply { nextToken; nextToken.should.equal(JSONToken.booleanValue) }
				reader("f").apply { nextToken; nextToken.should.equal(JSONToken.booleanValue) }
				reader("0").apply { nextToken; nextToken.should.equal(JSONToken.numberValue) }
				reader("1").apply { nextToken; nextToken.should.equal(JSONToken.numberValue) }
				reader("-").apply { nextToken; nextToken.should.equal(JSONToken.numberValue) }
				reader("[").apply { nextToken; nextToken.should.equal(JSONToken.listStart) }
				reader("\"").apply { nextToken; nextToken.should.equal(JSONToken.stringValue) }
				reader("{").apply { nextToken; nextToken.should.equal(JSONToken.mapStart) }
			}

			it("should be 'null' when done") {
				reader("0").apply { readInt(); nextToken.should.be.`null` }
			}
		}


		it("readBoolean()") {
			reader("true").readBoolean().should.be.`true`
			reader("false").readBoolean().should.be.`false`
		}


		it("readBooleanOrNull()") {
			reader("true").readBooleanOrNull().should.be.`true`
			reader("false").readBooleanOrNull().should.be.`false`
			reader("null").readBooleanOrNull().should.be.`null`
		}


		it("readDouble()") {
			reader("0").readDouble().should.equal(0.0)
			reader("-0").readDouble().should.equal(-0.0)
			reader("100").readDouble().should.equal(100.0)
			reader("-100").readDouble().should.equal(-100.0)
			reader("0.000").readDouble().should.equal(0.0)
			reader("-0.000").readDouble().should.equal(-0.0)
			reader("100.001").readDouble().should.equal(100.001)
			reader("-100.001").readDouble().should.equal(-100.001)
			reader("100.999").readDouble().should.equal(100.999)
			reader("-100.999").readDouble().should.equal(-100.999)
			reader("1e2").readDouble().should.equal(100.0)
			reader("-1e2").readDouble().should.equal(-100.0)
			reader("1.0e2").readDouble().should.equal(100.0)
			reader("-1.0e2").readDouble().should.equal(-100.0)
			reader("1.0e+2").readDouble().should.equal(100.0)
			reader("-1.0e+2").readDouble().should.equal(-100.0)
			reader("1.0e-2").readDouble().should.equal(0.01)
			reader("-1.0e-2").readDouble().should.equal(-0.01)
			reader("9223372036854775808").readDouble().should.equal(9223372036854775808.0)
			reader("-9223372036854775809").readDouble().should.equal(-9223372036854775809.0)
			reader("1000000000000000000000000000000").readDouble().should.equal(1000000000000000000000000000000.0)
			reader("-1000000000000000000000000000000").readDouble().should.equal(-1000000000000000000000000000000.0)
			reader("1e20000").readDouble().should.equal(Double.POSITIVE_INFINITY)
			reader("-1e20000").readDouble().should.equal(Double.NEGATIVE_INFINITY)
		}


		it("readDoubleOrNull()") {
			reader("0").readDoubleOrNull().should.equal(0.0)
			reader("-0").readDoubleOrNull().should.equal(-0.0)
			reader("100").readDoubleOrNull().should.equal(100.0)
			reader("-100").readDoubleOrNull().should.equal(-100.0)
			reader("0.000").readDoubleOrNull().should.equal(0.0)
			reader("-0.000").readDoubleOrNull().should.equal(-0.0)
			reader("100.001").readDoubleOrNull().should.equal(100.001)
			reader("-100.001").readDoubleOrNull().should.equal(-100.001)
			reader("100.999").readDoubleOrNull().should.equal(100.999)
			reader("-100.999").readDoubleOrNull().should.equal(-100.999)
			reader("1e2").readDoubleOrNull().should.equal(100.0)
			reader("-1e2").readDoubleOrNull().should.equal(-100.0)
			reader("1.0e2").readDoubleOrNull().should.equal(100.0)
			reader("-1.0e2").readDoubleOrNull().should.equal(-100.0)
			reader("1.0e+2").readDoubleOrNull().should.equal(100.0)
			reader("-1.0e+2").readDoubleOrNull().should.equal(-100.0)
			reader("1.0e-2").readDoubleOrNull().should.equal(0.01)
			reader("-1.0e-2").readDoubleOrNull().should.equal(-0.01)
			reader("9223372036854775808").readDoubleOrNull().should.equal(9223372036854775808.0)
			reader("-9223372036854775809").readDoubleOrNull().should.equal(-9223372036854775809.0)
			reader("1000000000000000000000000000000").readDoubleOrNull().should.equal(1000000000000000000000000000000.0)
			reader("-1000000000000000000000000000000").readDoubleOrNull().should.equal(-1000000000000000000000000000000.0)
			reader("1e20000").readDoubleOrNull().should.equal(Double.POSITIVE_INFINITY)
			reader("-1e20000").readDoubleOrNull().should.equal(Double.NEGATIVE_INFINITY)
			reader("null").readDoubleOrNull().should.be.`null`
		}


		it("readFloat()") {
			reader("0").readFloat().should.equal(0.0f)
			reader("-0").readFloat().should.equal(-0.0f)
			reader("100").readFloat().should.equal(100.0f)
			reader("-100").readFloat().should.equal(-100.0f)
			reader("0.000").readFloat().should.equal(0.0f)
			reader("-0.000").readFloat().should.equal(-0.0f)
			reader("100.001").readFloat().should.equal(100.001f)
			reader("-100.001").readFloat().should.equal(-100.001f)
			reader("100.999").readFloat().should.equal(100.999f)
			reader("-100.999").readFloat().should.equal(-100.999f)
			reader("1e2").readFloat().should.equal(100.0f)
			reader("-1e2").readFloat().should.equal(-100.0f)
			reader("1.0e2").readFloat().should.equal(100.0f)
			reader("-1.0e2").readFloat().should.equal(-100.0f)
			reader("1.0e+2").readFloat().should.equal(100.0f)
			reader("-1.0e+2").readFloat().should.equal(-100.0f)
			reader("1.0e-2").readFloat().should.equal(0.01f)
			reader("-1.0e-2").readFloat().should.equal(-0.01f)
			reader("9223372036854775808").readFloat().should.equal(9223372036854775808.0f)
			reader("-9223372036854775809").readFloat().should.equal(-9223372036854775809.0f)
			reader("1000000000000000000000000000000").readFloat().should.equal(1000000000000000000000000000000.0f)
			reader("-1000000000000000000000000000000").readFloat().should.equal(-1000000000000000000000000000000.0f)
			reader("1e20000").readFloat().should.equal(Float.POSITIVE_INFINITY)
			reader("-1e20000").readFloat().should.equal(Float.NEGATIVE_INFINITY)
		}


		it("readFloatOrNull()") {
			reader("0").readFloatOrNull().should.equal(0.0f)
			reader("-0").readFloatOrNull().should.equal(-0.0f)
			reader("100").readFloatOrNull().should.equal(100.0f)
			reader("-100").readFloatOrNull().should.equal(-100.0f)
			reader("0.000").readFloatOrNull().should.equal(0.0f)
			reader("-0.000").readFloatOrNull().should.equal(-0.0f)
			reader("100.001").readFloatOrNull().should.equal(100.001f)
			reader("-100.001").readFloatOrNull().should.equal(-100.001f)
			reader("100.999").readFloatOrNull().should.equal(100.999f)
			reader("-100.999").readFloatOrNull().should.equal(-100.999f)
			reader("1e2").readFloatOrNull().should.equal(100.0f)
			reader("-1e2").readFloatOrNull().should.equal(-100.0f)
			reader("1.0e2").readFloatOrNull().should.equal(100.0f)
			reader("-1.0e2").readFloatOrNull().should.equal(-100.0f)
			reader("1.0e+2").readFloatOrNull().should.equal(100.0f)
			reader("-1.0e+2").readFloatOrNull().should.equal(-100.0f)
			reader("1.0e-2").readFloatOrNull().should.equal(0.01f)
			reader("-1.0e-2").readFloatOrNull().should.equal(-0.01f)
			reader("9223372036854775808").readFloatOrNull().should.equal(9223372036854775808.0f)
			reader("-9223372036854775809").readFloatOrNull().should.equal(-9223372036854775809.0f)
			reader("1000000000000000000000000000000").readFloatOrNull().should.equal(1000000000000000000000000000000.0f)
			reader("-1000000000000000000000000000000").readFloatOrNull().should.equal(-1000000000000000000000000000000.0f)
			reader("1e20000").readFloatOrNull().should.equal(Float.POSITIVE_INFINITY)
			reader("-1e20000").readFloatOrNull().should.equal(Float.NEGATIVE_INFINITY)
			reader("null").readFloatOrNull().should.be.`null`
		}


		it("readInt()") {
			reader("0").readInt().should.equal(0)
			reader("-0").readInt().should.equal(0)
			reader("100").readInt().should.equal(100)
			reader("-100").readInt().should.equal(-100)
			reader("0.000").readInt().should.equal(0)
			reader("-0.000").readInt().should.equal(0)
			reader("100.001").readInt().should.equal(100)
			reader("-100.001").readInt().should.equal(-100)
			reader("100.999").readInt().should.equal(100)
			reader("-100.999").readInt().should.equal(-100)
			reader("1e2").readInt().should.equal(100)
			reader("-1e2").readInt().should.equal(-100)
			reader("1.0e+2").readInt().should.equal(100)
			reader("-1.0e+2").readInt().should.equal(-100)
			reader("1.0e2").readInt().should.equal(100)
			reader("-1.0e2").readInt().should.equal(-100)
			reader("1.0e-2").readInt().should.equal(0)
			reader("-1.0e-2").readInt().should.equal(0)
		}


		it("readIntOrNull()") {
			reader("0").readIntOrNull().should.equal(0)
			reader("-0").readIntOrNull().should.equal(0)
			reader("100").readIntOrNull().should.equal(100)
			reader("-100").readIntOrNull().should.equal(-100)
			reader("0.000").readIntOrNull().should.equal(0)
			reader("-0.000").readIntOrNull().should.equal(0)
			reader("100.001").readIntOrNull().should.equal(100)
			reader("-100.001").readIntOrNull().should.equal(-100)
			reader("100.999").readIntOrNull().should.equal(100)
			reader("-100.999").readIntOrNull().should.equal(-100)
			reader("1e2").readIntOrNull().should.equal(100)
			reader("-1e2").readIntOrNull().should.equal(-100)
			reader("1.0e2").readIntOrNull().should.equal(100)
			reader("-1.0e2").readIntOrNull().should.equal(-100)
			reader("1.0e+2").readIntOrNull().should.equal(100)
			reader("-1.0e+2").readIntOrNull().should.equal(-100)
			reader("1.0e-2").readIntOrNull().should.equal(0)
			reader("-1.0e-2").readIntOrNull().should.equal(0)
			reader("null").readIntOrNull().should.be.`null`
		}


		it("readList()") {
			reader("[]").readList().should.equal(emptyList())
			reader("[ \t\n\r]").readList().should.equal(emptyList())
			reader("[1]").readList().should.equal(listOf(1))
			reader("[ true, \"hey\", null ]").readList().should.equal(listOf(true, "hey", null))
			reader("[ [], [ 1 ] ]").readList().should.equal(listOf(emptyList<Any?>(), listOf(1)))
		}


		it("readList() inline") {
			reader("[]").apply {
				readList { Dummy1 }.should.equal(Dummy1)
			}

			reader("[ \t\n\r]").apply {
				readList { Dummy1 }.should.equal(Dummy1)
			}

			reader("[1]").apply {
				readList {
					skipValue()
					Dummy1
				}.should.equal(Dummy1)
			}

			reader("[ true, \"hey\", null ]").apply {
				readList {
					skipValue()
					skipValue()
					skipValue()
					Dummy1
				}.should.equal(Dummy1)
			}

			reader("[ [], [ 1 ] ]").apply {
				readList {
					readList { Dummy2 }.should.equal(Dummy2)
					readList {
						skipValue()
						Dummy3
					}.should.equal(Dummy3)
					Dummy1
				}.should.equal(Dummy1)
			}
		}


		it("readListByElement()") {
			val list = mutableListOf<Any?>()

			reader("[ true, \"hey\", null ]").readListByElement {
				list += readValue()
			}

			list.should.equal(listOf(true, "hey", null))
		}


		it("readListEnd()") {
			reader("[]").apply {
				readListStart()
				nextToken.should.equal(JSONToken.listEnd)
				readListEnd()
				nextToken.should.be.`null`
			}
		}


		it("readListOrNull()") {
			reader("[]").readListOrNull().should.equal(emptyList())
			reader("[ \t\n\r]").readListOrNull().should.equal(emptyList())
			reader("[1]").readListOrNull().should.equal(listOf(1))
			reader("[ true, \"hey\", null ]").readListOrNull().should.equal(listOf(true, "hey", null))
			reader("[ [], [ 1 ] ]").readListOrNull().should.equal(listOf(emptyList<Any?>(), listOf(1)))
			reader("null").readListOrNull().should.be.`null`
		}


		it("readListOrNull() inline") {
			reader("[]").apply {
				readListOrNull { Dummy1 }.should.equal(Dummy1)
			}

			reader("[ \t\n\r]").apply {
				readListOrNull { Dummy1 }.should.equal(Dummy1)
			}

			reader("[1]").apply {
				readListOrNull {
					skipValue()
					Dummy1
				}.should.equal(Dummy1)
			}

			reader("[ true, \"hey\", null ]").apply {
				readListOrNull {
					skipValue()
					skipValue()
					skipValue()
					Dummy1
				}.should.equal(Dummy1)
			}

			reader("[ [], [ 1 ] ]").apply {
				readListOrNull {
					readListOrNull { Dummy2 }.should.equal(Dummy2)
					readListOrNull {
						skipValue()
						Dummy3
					}.should.equal(Dummy3)
					Dummy1
				}.should.equal(Dummy1)
			}

			reader("null").readListOrNull { Dummy1 }.should.be.`null`
		}


		it("readListStart()") {
			reader("[]").apply {
				nextToken.should.equal(JSONToken.listStart)
				readListStart()
				nextToken.should.equal(JSONToken.listEnd)
			}
		}


		it("readLong()") {
			reader("0").readLong().should.equal(0L)
			reader("-0").readLong().should.equal(0L)
			reader("100").readLong().should.equal(100L)
			reader("-100").readLong().should.equal(-100L)
			reader("0.000").readLong().should.equal(0L)
			reader("-0.000").readLong().should.equal(0L)
			reader("100.001").readLong().should.equal(100L)
			reader("-100.001").readLong().should.equal(-100L)
			reader("100.999").readLong().should.equal(100L)
			reader("-100.999").readLong().should.equal(-100L)
			reader("1e2").readLong().should.equal(100L)
			reader("-1e2").readLong().should.equal(-100L)
			reader("1.0e2").readLong().should.equal(100L)
			reader("-1.0e2").readLong().should.equal(-100L)
			reader("1.0e+2").readLong().should.equal(100L)
			reader("-1.0e+2").readLong().should.equal(-100L)
			reader("1.0e-2").readLong().should.equal(0L)
			reader("-1.0e-2").readLong().should.equal(0L)
		}


		it("readLongOrNull()") {
			reader("0").readLongOrNull().should.equal(0L)
			reader("-0").readLongOrNull().should.equal(0L)
			reader("100").readLongOrNull().should.equal(100L)
			reader("-100").readLongOrNull().should.equal(-100L)
			reader("0.000").readLongOrNull().should.equal(0L)
			reader("-0.000").readLongOrNull().should.equal(0L)
			reader("100.001").readLongOrNull().should.equal(100L)
			reader("-100.001").readLongOrNull().should.equal(-100L)
			reader("100.999").readLongOrNull().should.equal(100L)
			reader("-100.999").readLongOrNull().should.equal(-100L)
			reader("1e2").readLongOrNull().should.equal(100L)
			reader("-1e2").readLongOrNull().should.equal(-100L)
			reader("1.0e2").readLongOrNull().should.equal(100L)
			reader("-1.0e2").readLongOrNull().should.equal(-100L)
			reader("1.0e+2").readLongOrNull().should.equal(100L)
			reader("-1.0e+2").readLongOrNull().should.equal(-100L)
			reader("1.0e-2").readLongOrNull().should.equal(0L)
			reader("-1.0e-2").readLongOrNull().should.equal(0L)
			reader("null").readLongOrNull().should.be.`null`
		}


		it("readMap()") {
			reader("{}").readMap().should.equal(emptyMap<String, Any>())
			reader("{ \t\n\r}").readMap().should.equal(emptyMap<String, Any>())
			reader("{\"key\":1}").readMap().should.equal(mapOf("key" to 1))
			reader("{ \"key0\": true, \"key1\" :\"hey\", \"key2\" : null }").readMap().should.equal(mapOf(
				"key0" to true,
				"key1" to "hey",
				"key2" to null
			))
			reader("{ \"key0\": {}, \"key1\": { \"key\": 1 } }").readMap().should.equal(mapOf(
				"key0" to emptyMap<String, Any>(),
				"key1" to mapOf("key" to 1)
			))
			reader("{ \" \\\\ \\\" \\/ \\b \\f \\n \\r \\t \\uD83D\\udc36 \": 1 }").readMap().should.equal(mapOf(
				" \\ \" / \b \u000C \n \r \t 🐶 " to 1
			))
			reader("{ \"0\": 0, \"2\": 2, \"1\": 1, \"3\": 3, \"-1\": -1 }").readMap().toList().should.equal(listOf(
				"0" to 0,
				"2" to 2,
				"1" to 1,
				"3" to 3,
				"-1" to -1
			))
		}


		it("readMap() inline") {
			reader("{}").apply {
				readMap { Dummy1 }.should.equal(Dummy1)
			}
			reader("{ \t\n\r}").apply {
				readMap { Dummy1 }.should.equal(Dummy1)
			}
			reader("{\"key\":1}").apply {
				readMap {
					readString().should.equal("key")
					readInt().should.equal(1)
					Dummy1
				}.should.equal(Dummy1)
			}
			reader("{ \"key0\": true, \"key1\" :\"hey\", \"key2\" : null }").apply {
				readMap {
					readString().should.equal("key0")
					readBoolean().should.equal(true)
					readString().should.equal("key1")
					readString().should.equal("hey")
					readString().should.equal("key2")
					readNull()
					Dummy1
				}.should.equal(Dummy1)
			}
			reader("{ \"key0\": {}, \"key1\": { \"key\": 1 } }").apply {
				readMap {
					readString().should.equal("key0")
					readMap { Dummy2 }.should.equal(Dummy2)
					readString().should.equal("key1")
					readMap {
						readString().should.equal("key")
						readInt().should.equal(1)
						Dummy3
					}.should.equal(Dummy3)
					Dummy1
				}.should.equal(Dummy1)
			}
			reader("{ \" \\\\ \\\" \\/ \\b \\f \\n \\r \\t \\uD83D\\udc36 \": 1 }").apply {
				readMap {
					readString().should.equal(" \\ \" / \b \u000C \n \r \t 🐶 ")
					readInt().should.equal(1)
					Dummy1
				}.should.equal(Dummy1)
			}
			reader("{ \"0\": 0, \"2\": 2, \"1\": 1, \"3\": 3, \"-1\": -1 }").apply {
				readMap {
					readString().should.equal("0")
					readInt().should.equal(0)
					readString().should.equal("2")
					readInt().should.equal(2)
					readString().should.equal("1")
					readInt().should.equal(1)
					readString().should.equal("3")
					readInt().should.equal(3)
					readString().should.equal("-1")
					readInt().should.equal(-1)
					Dummy1
				}.should.equal(Dummy1)
			}
		}


		it("readMapByEntry()") {
			val map = mutableMapOf<String, Any?>()

			reader("{ \"key0\": true, \"key1\" :\"hey\", \"key2\" : null }").readMapByEntry { key ->
				map[key] = readValue()
			}

			map.should.equal(mapOf(
				"key0" to true,
				"key1" to "hey",
				"key2" to null
			))
		}


		it("readMapEnd()") {
			reader("{}").apply {
				readMapStart()
				nextToken.should.equal(JSONToken.mapEnd)
				readMapEnd()
				nextToken.should.be.`null`
			}
		}


		it("readMapKey()") {
			reader("{\"\"").apply {
				readMapStart()
				readMapKey().should.equal("")
			}
			reader("{\"simple\"").apply {
				readMapStart()
				readMapKey().should.equal("simple")
			}
			reader("{\" a bit longer \"").apply {
				readMapStart()
				readMapKey().should.equal(" a bit longer ")
			}
			reader("{\"a dog: 🐶\"").apply {
				readMapStart()
				readMapKey().should.equal("a dog: 🐶")
			}
			reader("{\" \\\\ \\\" \\/ \\b \\f \\n \\r \\t \\uD83D\\udc36 \"").apply {
				readMapStart()
				readMapKey().should.equal(" \\ \" / \b \u000C \n \r \t 🐶 ")
			}
		}


		it("readMapOrNull()") {
			reader("{}").readMapOrNull().should.equal(emptyMap<String, Any>())
			reader("{ \t\n\r}").readMapOrNull().should.equal(emptyMap<String, Any>())
			reader("{\"key\":1}").readMapOrNull().should.equal(mapOf("key" to 1))
			reader("{ \"key0\": true, \"key1\" :\"hey\", \"key2\" : null }").readMapOrNull().should.equal(mapOf(
				"key0" to true,
				"key1" to "hey",
				"key2" to null
			))
			reader("{ \"key0\": {}, \"key1\": { \"key\": 1 } }").readMapOrNull().should.equal(mapOf(
				"key0" to emptyMap<String, Any>(),
				"key1" to mapOf("key" to 1)
			))
			reader("{ \" \\\\ \\\" \\/ \\b \\f \\n \\r \\t \\uD83D\\udc36 \": 1 }").readMapOrNull().should.equal(mapOf(
				" \\ \" / \b \u000C \n \r \t 🐶 " to 1
			))
			reader("{ \"0\": 0, \"2\": 2, \"1\": 1, \"3\": 3, \"-1\": -1 }").readMapOrNull()?.toList().should.equal(listOf(
				"0" to 0,
				"2" to 2,
				"1" to 1,
				"3" to 3,
				"-1" to -1
			))
			reader("null").readMapOrNull().should.be.`null`
		}


		it("readMapOrNull() inline") {
			reader("{}").apply {
				readMapOrNull { Dummy1 }.should.equal(Dummy1)
			}
			reader("{ \t\n\r}").apply {
				readMapOrNull { Dummy1 }.should.equal(Dummy1)
			}
			reader("{\"key\":1}").apply {
				readMapOrNull {
					readString().should.equal("key")
					readInt().should.equal(1)
					Dummy1
				}.should.equal(Dummy1)
			}
			reader("{ \"key0\": true, \"key1\" :\"hey\", \"key2\" : null }").apply {
				readMapOrNull {
					readString().should.equal("key0")
					readBoolean().should.equal(true)
					readString().should.equal("key1")
					readString().should.equal("hey")
					readString().should.equal("key2")
					readNull()
					Dummy1
				}.should.equal(Dummy1)
			}
			reader("{ \"key0\": {}, \"key1\": { \"key\": 1 } }").apply {
				readMapOrNull {
					readString().should.equal("key0")
					readMapOrNull { Dummy2 }.should.equal(Dummy2)
					readString().should.equal("key1")
					readMapOrNull {
						readString().should.equal("key")
						readInt().should.equal(1)
						Dummy3
					}.should.equal(Dummy3)
					Dummy1
				}.should.equal(Dummy1)
			}
			reader("{ \" \\\\ \\\" \\/ \\b \\f \\n \\r \\t \\uD83D\\udc36 \": 1 }").apply {
				readMapOrNull {
					readString().should.equal(" \\ \" / \b \u000C \n \r \t 🐶 ")
					readInt().should.equal(1)
					Dummy1
				}.should.equal(Dummy1)
			}
			reader("{ \"0\": 0, \"2\": 2, \"1\": 1, \"3\": 3, \"-1\": -1 }").apply {
				readMapOrNull {
					readString().should.equal("0")
					readInt().should.equal(0)
					readString().should.equal("2")
					readInt().should.equal(2)
					readString().should.equal("1")
					readInt().should.equal(1)
					readString().should.equal("3")
					readInt().should.equal(3)
					readString().should.equal("-1")
					readInt().should.equal(-1)
					Dummy1
				}.should.equal(Dummy1)
			}
			reader("null").apply {
				readMapOrNull { Dummy1 }.should.be.`null`
			}
		}


		it("readMapStart()") {
			reader("{}").apply {
				nextToken.should.equal(JSONToken.mapStart)
				readMapStart()
				nextToken.should.equal(JSONToken.mapEnd)
			}
		}


		it("readNull()") {
			(reader("null").readNull() as Any?).should.be.`null`
		}


		it("readNumber()") {
			reader("0").readNumber().should.equal(0)
			reader("-0").readNumber().should.equal(0)
			reader("100").readNumber().should.equal(100)
			reader("-100").readNumber().should.equal(-100)
			reader("0.000").readNumber().should.equal(0.0)
			reader("-0.000").readNumber().should.equal(-0.0)
			reader("100.001").readNumber().should.equal(100.001)
			reader("-100.001").readNumber().should.equal(-100.001)
			reader("100.999").readNumber().should.equal(100.999)
			reader("-100.999").readNumber().should.equal(-100.999)
			reader("2147483647").readNumber().should.equal(2147483647)
			reader("-2147483648").readNumber().should.equal(-2147483648)
			reader("2147483648").readNumber().should.equal(2147483648L)
			reader("-2147483649").readNumber().should.equal(-2147483649L)
			reader("2147483647").readNumber().should.equal(2147483647)
			reader("-2147483648").readNumber().should.equal(-2147483648)
			reader("9223372036854775807").readNumber().should.equal(9223372036854775807L)
			reader("-9223372036854775808").readNumber().should.equal(-9223372036854775807L - 1) // https://youtrack.jetbrains.com/issue/KT-17172
			reader("9223372036854775808").readNumber().should.equal(9223372036854775808.0)
			reader("-9223372036854775809").readNumber().should.equal(-9223372036854775809.0)
			reader("1e2").readNumber().should.equal(100.0)
			reader("-1e2").readNumber().should.equal(-100.0)
			reader("1.0e2").readNumber().should.equal(100.0)
			reader("-1.0e2").readNumber().should.equal(-100.0)
			reader("1.0e-2").readNumber().should.equal(0.01)
			reader("-1.0e-2").readNumber().should.equal(-0.01)
			reader("1000000000000000000000000000000").readNumber().should.equal(1000000000000000000000000000000.0)
			reader("-1000000000000000000000000000000").readNumber().should.equal(-1000000000000000000000000000000.0)
			reader("1e20000").readNumber().should.equal(Double.POSITIVE_INFINITY)
			reader("-1e20000").readNumber().should.equal(Double.NEGATIVE_INFINITY)
		}


		it("readNumberOrNull()") {
			reader("0").readNumberOrNull().should.equal(0)
			reader("-0").readNumberOrNull().should.equal(0)
			reader("100").readNumberOrNull().should.equal(100)
			reader("-100").readNumberOrNull().should.equal(-100)
			reader("0.000").readNumberOrNull().should.equal(0.0)
			reader("-0.000").readNumberOrNull().should.equal(-0.0)
			reader("100.001").readNumberOrNull().should.equal(100.001)
			reader("-100.001").readNumberOrNull().should.equal(-100.001)
			reader("100.999").readNumberOrNull().should.equal(100.999)
			reader("-100.999").readNumberOrNull().should.equal(-100.999)
			reader("2147483647").readNumberOrNull().should.equal(2147483647)
			reader("-2147483648").readNumberOrNull().should.equal(-2147483648)
			reader("2147483648").readNumberOrNull().should.equal(2147483648L)
			reader("-2147483649").readNumberOrNull().should.equal(-2147483649L)
			reader("2147483647").readNumberOrNull().should.equal(2147483647)
			reader("-2147483648").readNumberOrNull().should.equal(-2147483648)
			reader("9223372036854775807").readNumberOrNull().should.equal(9223372036854775807L)
			reader("-9223372036854775808").readNumberOrNull().should.equal(-9223372036854775807L - 1) // https://youtrack.jetbrains.com/issue/KT-17172
			reader("9223372036854775808").readNumberOrNull().should.equal(9223372036854775808.0)
			reader("-9223372036854775809").readNumberOrNull().should.equal(-9223372036854775809.0)
			reader("1e2").readNumberOrNull().should.equal(100.0)
			reader("-1e2").readNumberOrNull().should.equal(-100.0)
			reader("1.0e2").readNumberOrNull().should.equal(100.0)
			reader("-1.0e2").readNumberOrNull().should.equal(-100.0)
			reader("1.0e-2").readNumberOrNull().should.equal(0.01)
			reader("-1.0e-2").readNumberOrNull().should.equal(-0.01)
			reader("1000000000000000000000000000000").readNumberOrNull().should.equal(1000000000000000000000000000000.0)
			reader("-1000000000000000000000000000000").readNumberOrNull().should.equal(-1000000000000000000000000000000.0)
			reader("1e20000").readNumberOrNull().should.equal(Double.POSITIVE_INFINITY)
			reader("-1e20000").readNumberOrNull().should.equal(Double.NEGATIVE_INFINITY)
			reader("null").readNumberOrNull().should.be.`null`
		}


		it("readString()") {
			reader("\"\"").readString().should.equal("")
			reader("\"\\u0022\"").readString().should.equal("\"")
			reader("\"simple\"").readString().should.equal("simple")
			reader("\" a bit longer \"").readString().should.equal(" a bit longer ")
			reader("\"a dog: 🐶\"").readString().should.equal("a dog: 🐶")
			reader("\" \\\\ \\\" \\/ \\b \\f \\n \\r \\t \\uD83D\\udc36 \"").readString().should.equal(" \\ \" / \b \u000C \n \r \t 🐶 ")
			reader("{\"key\"").apply { readMapStart(); readString().should.equal("key") }
		}


		it("readStringOrNull()") {
			reader("\"\"").readStringOrNull().should.equal("")
			reader("\"\\u0022\"").readStringOrNull().should.equal("\"")
			reader("\"simple\"").readStringOrNull().should.equal("simple")
			reader("\" a bit longer \"").readStringOrNull().should.equal(" a bit longer ")
			reader("\"a dog: 🐶\"").readStringOrNull().should.equal("a dog: 🐶")
			reader("\" \\\\ \\\" \\/ \\b \\f \\n \\r \\t \\uD83D\\udc36 \"").readStringOrNull().should.equal(" \\ \" / \b \u000C \n \r \t 🐶 ")
			reader("null").readStringOrNull().should.be.`null`
		}


		it("readValue()") {
			reader("[null, true, 0, [], {}, \"\"]").apply {
				readList {
					readValue().should.be.`null`
					readValue().should.equal(true)
					readValue().should.equal(0)
					readValue().should.equal(emptyList<Any?>())
					readValue().should.equal(emptyMap<String, Any?>())
					readValue().should.equal("")
				}
			}
		}


		it("skipValue()") {
			reader("[null, true, 0, [], {}, \"\"]").apply {
				readList {
					nextToken.should.equal(JSONToken.nullValue)
					skipValue()
					nextToken.should.equal(JSONToken.booleanValue)
					skipValue()
					nextToken.should.equal(JSONToken.numberValue)
					skipValue()
					nextToken.should.equal(JSONToken.listStart)
					skipValue()
					nextToken.should.equal(JSONToken.mapStart)
					skipValue()
					nextToken.should.equal(JSONToken.stringValue)
					skipValue()
				}
			}
		}
	}
}) {

	private object Dummy1
	private object Dummy2
	private object Dummy3
}


// TODO move the following methods inside the object above once KT-19796 is fixed
// https://youtrack.jetbrains.com/issue/KT-19796

private fun reader(string: String): JSONReader =
	TextInputReader(TextInput(StringReader(string)))
