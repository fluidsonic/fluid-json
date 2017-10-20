package tests

import com.github.fluidsonic.fluid.json.JSONException
import com.github.fluidsonic.fluid.json.JSONReader
import com.github.fluidsonic.fluid.json.StandardReader
import com.github.fluidsonic.fluid.json.TextInput
import com.github.fluidsonic.fluid.json.readBooleanOrNull
import com.github.fluidsonic.fluid.json.readDoubleOrNull
import com.github.fluidsonic.fluid.json.readElementsFromMap
import com.github.fluidsonic.fluid.json.readFloatOrNull
import com.github.fluidsonic.fluid.json.readFromList
import com.github.fluidsonic.fluid.json.readIntOrNull
import com.github.fluidsonic.fluid.json.readList
import com.github.fluidsonic.fluid.json.readListByElement
import com.github.fluidsonic.fluid.json.readListOrNull
import com.github.fluidsonic.fluid.json.readLongOrNull
import com.github.fluidsonic.fluid.json.readMap
import com.github.fluidsonic.fluid.json.readMapOrNull
import com.github.fluidsonic.fluid.json.readNumberOrNull
import com.github.fluidsonic.fluid.json.readStringOrNull
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.TestBody
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.io.StringReader


internal object StandardReaderRejectSpec : Spek({

	describe("StandardReader fails in") {

		it(".nextToken") {
			readerShouldFail("a") { nextToken }
			readerShouldFail("N") { nextToken }
			readerShouldFail("F") { nextToken }
			readerShouldFail("T") { nextToken }
			readerShouldFail("]") { nextToken }
			readerShouldFail("}") { nextToken }
			readerShouldFail("+") { nextToken }
			readerShouldFail(";") { nextToken }
			readerShouldFail(",") { nextToken }
			readerShouldFail(":") { nextToken }
			readerShouldFail("/") { nextToken }
			readerShouldFail("\\") { nextToken }
			readerShouldFail("") { nextToken }
			readerShouldFail("null;") { skipValue(); nextToken }
			readerShouldFail("null null") { skipValue(); nextToken }
			readerShouldFail("null,null") { skipValue(); nextToken }
		}

		it("readBoolean()") {
			readerShouldFail("") { readBoolean() }
			readerShouldFail("f") { readBoolean() }
			readerShouldFail("t") { readBoolean() }
			readerShouldFail("falsef") { readBoolean() }
			readerShouldFail("truet") { readBoolean() }
			readerShouldFail("false2") { readBoolean() }
			readerShouldFail("true2") { readBoolean() }
			readerShouldFail("false\"") { readBoolean() }
			readerShouldFail("true\"") { readBoolean() }
			readerShouldFail("0") { readBoolean() }
			readerShouldFail("\"\"") { readBoolean() }
			readerShouldFail("[]") { readBoolean() }
			readerShouldFail("{}") { readBoolean() }
			readerShouldFail("null") { readBoolean() }
		}

		it("readBooleanOrNull()") {
			readerShouldFail("") { readBooleanOrNull() }
			readerShouldFail("f") { readBooleanOrNull() }
			readerShouldFail("t") { readBooleanOrNull() }
			readerShouldFail("falsef") { readBooleanOrNull() }
			readerShouldFail("truet") { readBooleanOrNull() }
			readerShouldFail("false2") { readBooleanOrNull() }
			readerShouldFail("true2") { readBooleanOrNull() }
			readerShouldFail("false\"") { readBooleanOrNull() }
			readerShouldFail("true\"") { readBooleanOrNull() }
			readerShouldFail("0") { readBooleanOrNull() }
			readerShouldFail("\"\"") { readBooleanOrNull() }
			readerShouldFail("[]") { readBooleanOrNull() }
			readerShouldFail("{}") { readBooleanOrNull() }
		}

		it("readDouble()") {
			readerShouldFail("") { readDouble() }
			readerShouldFail("0b0") { readDouble() }
			readerShouldFail("0o0") { readDouble() }
			readerShouldFail("0x0") { readDouble() }
			readerShouldFail("01") { readDouble() }
			readerShouldFail("01.0") { readDouble() }
			readerShouldFail("+0") { readDouble() }
			readerShouldFail("+1") { readDouble() }
			readerShouldFail("-+1") { readDouble() }
			readerShouldFail("-true") { readDouble() }
			readerShouldFail("-.") { readDouble() }
			readerShouldFail("-e") { readDouble() }
			readerShouldFail("0.") { readDouble() }
			readerShouldFail("0e") { readDouble() }
			readerShouldFail("1.") { readDouble() }
			readerShouldFail("1.e") { readDouble() }
			readerShouldFail("1e") { readDouble() }
			readerShouldFail("1ee") { readDouble() }
			readerShouldFail("1e.") { readDouble() }
			readerShouldFail("1e+") { readDouble() }
			readerShouldFail("1e+e") { readDouble() }
			readerShouldFail("1e-") { readDouble() }
			readerShouldFail("1e-e") { readDouble() }
			readerShouldFail("null") { readDouble() }
		}

		it("readDoubleOrNull()") {
			readerShouldFail("") { readDoubleOrNull() }
			readerShouldFail("0b0") { readDoubleOrNull() }
			readerShouldFail("0o0") { readDoubleOrNull() }
			readerShouldFail("0x0") { readDoubleOrNull() }
			readerShouldFail("01") { readDoubleOrNull() }
			readerShouldFail("01.0") { readDoubleOrNull() }
			readerShouldFail("+0") { readDoubleOrNull() }
			readerShouldFail("+1") { readDoubleOrNull() }
			readerShouldFail("-+1") { readDoubleOrNull() }
			readerShouldFail("-true") { readDoubleOrNull() }
			readerShouldFail("-.") { readDoubleOrNull() }
			readerShouldFail("-e") { readDoubleOrNull() }
			readerShouldFail("0.") { readDoubleOrNull() }
			readerShouldFail("0e") { readDoubleOrNull() }
			readerShouldFail("1.") { readDoubleOrNull() }
			readerShouldFail("1.e") { readDoubleOrNull() }
			readerShouldFail("1e") { readDoubleOrNull() }
			readerShouldFail("1ee") { readDoubleOrNull() }
			readerShouldFail("1e.") { readDoubleOrNull() }
			readerShouldFail("1e+") { readDoubleOrNull() }
			readerShouldFail("1e+e") { readDoubleOrNull() }
			readerShouldFail("1e-") { readDoubleOrNull() }
			readerShouldFail("1e-e") { readDoubleOrNull() }
		}

		it("readElementsFromMap()") {
			readerShouldFail("") { readElementsFromMap { skipValue() } }
			readerShouldFail("{") { readElementsFromMap { skipValue() } }
			readerShouldFail("{\"") { readElementsFromMap { skipValue() } }
			readerShouldFail("{\"x") { readElementsFromMap { skipValue() } }
			readerShouldFail("{\"x\"") { readElementsFromMap { skipValue() } }
			readerShouldFail("{\"x\":") { readElementsFromMap { skipValue() } }
			readerShouldFail("{\"x\":1") { readElementsFromMap { skipValue() } }
			readerShouldFail("{\"x\":1,") { readElementsFromMap { skipValue() } }
			readerShouldFail("{true \"key\": 1}") { readElementsFromMap { skipValue() } }
			readerShouldFail("{\"key\" true: 1}") { readElementsFromMap { skipValue() } }
			readerShouldFail("{\"key\": 1 true}") { readElementsFromMap { skipValue() } }
			readerShouldFail("{\"key\"::1}") { readElementsFromMap { skipValue() } }
			readerShouldFail("{\"key\":1:}") { readElementsFromMap { skipValue() } }
			readerShouldFail("{\"key\":1,}") { readElementsFromMap { skipValue() } }
			readerShouldFail("{,\"key\":1}") { readElementsFromMap { skipValue() } }
			readerShouldFail("{\"key0\":0,,\"key1\":1}") { readElementsFromMap { skipValue() } }
			readerShouldFail("null") { readElementsFromMap { skipValue() } }
		}

		it("readFloat()") {
			readerShouldFail("") { readFloat() }
			readerShouldFail("0b0") { readFloat() }
			readerShouldFail("0o0") { readFloat() }
			readerShouldFail("0x0") { readFloat() }
			readerShouldFail("01") { readFloat() }
			readerShouldFail("01.0") { readFloat() }
			readerShouldFail("+0") { readFloat() }
			readerShouldFail("+1") { readFloat() }
			readerShouldFail("-+1") { readFloat() }
			readerShouldFail("-true") { readFloat() }
			readerShouldFail("-.") { readFloat() }
			readerShouldFail("-e") { readFloat() }
			readerShouldFail("0.") { readFloat() }
			readerShouldFail("0e") { readFloat() }
			readerShouldFail("1.") { readFloat() }
			readerShouldFail("1.e") { readFloat() }
			readerShouldFail("1e") { readFloat() }
			readerShouldFail("1ee") { readFloat() }
			readerShouldFail("1e.") { readFloat() }
			readerShouldFail("1e+") { readFloat() }
			readerShouldFail("1e+e") { readFloat() }
			readerShouldFail("1e-") { readFloat() }
			readerShouldFail("1e-e") { readFloat() }
			readerShouldFail("null") { readFloat() }
		}

		it("readFloatOrNull()") {
			readerShouldFail("") { readFloatOrNull() }
			readerShouldFail("0b0") { readFloatOrNull() }
			readerShouldFail("0o0") { readFloatOrNull() }
			readerShouldFail("0x0") { readFloatOrNull() }
			readerShouldFail("01") { readFloatOrNull() }
			readerShouldFail("01.0") { readFloatOrNull() }
			readerShouldFail("+0") { readFloatOrNull() }
			readerShouldFail("+1") { readFloatOrNull() }
			readerShouldFail("-+1") { readFloatOrNull() }
			readerShouldFail("-true") { readFloatOrNull() }
			readerShouldFail("-.") { readFloatOrNull() }
			readerShouldFail("-e") { readFloatOrNull() }
			readerShouldFail("0.") { readFloatOrNull() }
			readerShouldFail("0e") { readFloatOrNull() }
			readerShouldFail("1.") { readFloatOrNull() }
			readerShouldFail("1.e") { readFloatOrNull() }
			readerShouldFail("1e") { readFloatOrNull() }
			readerShouldFail("1ee") { readFloatOrNull() }
			readerShouldFail("1e.") { readFloatOrNull() }
			readerShouldFail("1e+") { readFloatOrNull() }
			readerShouldFail("1e+e") { readFloatOrNull() }
			readerShouldFail("1e-") { readFloatOrNull() }
			readerShouldFail("1e-e") { readFloatOrNull() }
		}

		it("readInt()") {
			readerShouldFail("") { readInt() }
			readerShouldFail("0b0") { readInt() }
			readerShouldFail("0o0") { readInt() }
			readerShouldFail("0x0") { readInt() }
			readerShouldFail("01") { readInt() }
			readerShouldFail("01.0") { readInt() }
			readerShouldFail("+0") { readInt() }
			readerShouldFail("+1") { readInt() }
			readerShouldFail("-+1") { readInt() }
			readerShouldFail("-true") { readInt() }
			readerShouldFail("-.") { readInt() }
			readerShouldFail("-e") { readInt() }
			readerShouldFail("0.") { readInt() }
			readerShouldFail("0e") { readInt() }
			readerShouldFail("1.") { readInt() }
			readerShouldFail("1.e") { readInt() }
			readerShouldFail("1e") { readInt() }
			readerShouldFail("1ee") { readInt() }
			readerShouldFail("1e.") { readInt() }
			readerShouldFail("1e+") { readInt() }
			readerShouldFail("1e+e") { readInt() }
			readerShouldFail("1e-") { readInt() }
			readerShouldFail("1e-e") { readInt() }
			readerShouldFail("null") { readInt() }
		}

		it("readIntOrNull()") {
			readerShouldFail("") { readIntOrNull() }
			readerShouldFail("0b0") { readIntOrNull() }
			readerShouldFail("0o0") { readIntOrNull() }
			readerShouldFail("0x0") { readIntOrNull() }
			readerShouldFail("01") { readIntOrNull() }
			readerShouldFail("01.0") { readIntOrNull() }
			readerShouldFail("+0") { readIntOrNull() }
			readerShouldFail("+1") { readIntOrNull() }
			readerShouldFail("-+1") { readIntOrNull() }
			readerShouldFail("-true") { readIntOrNull() }
			readerShouldFail("-.") { readIntOrNull() }
			readerShouldFail("-e") { readIntOrNull() }
			readerShouldFail("0.") { readIntOrNull() }
			readerShouldFail("0e") { readIntOrNull() }
			readerShouldFail("1.") { readIntOrNull() }
			readerShouldFail("1.e") { readIntOrNull() }
			readerShouldFail("1e") { readIntOrNull() }
			readerShouldFail("1ee") { readIntOrNull() }
			readerShouldFail("1e.") { readIntOrNull() }
			readerShouldFail("1e+") { readIntOrNull() }
			readerShouldFail("1e+e") { readIntOrNull() }
			readerShouldFail("1e-") { readIntOrNull() }
			readerShouldFail("1e-e") { readIntOrNull() }
		}

		it("readList()") {
			readerShouldFail("") { readList() }
			readerShouldFail("[") { readList() }
			readerShouldFail("[1") { readList() }
			readerShouldFail("[1 1]") { readList() }
			readerShouldFail("[,1,1]") { readList() }
			readerShouldFail("[1,,1]") { readList() }
			readerShouldFail("[1,1,]") { readList() }
			readerShouldFail("[[1]") { readList() }
			readerShouldFail("null") { readList() }
		}

		it("readList() inline") {
			readerShouldFail("") { readFromList {} }
			readerShouldFail("[") { readFromList {} }
			readerShouldFail("[1") { readFromList {} }
			readerShouldFail("[1 1]") { readFromList {} }
			readerShouldFail("[,1,1]") { readFromList {} }
			readerShouldFail("[1,,1]") { readFromList {} }
			readerShouldFail("[1,1,]") { readFromList {} }
			readerShouldFail("[[1]") { readFromList {} }
			readerShouldFail("null") { readFromList {} }
		}

		it("readListByElement()") {
			readerShouldFail("") { readListByElement { skipValue() } }
			readerShouldFail("[") { readListByElement { skipValue() } }
			readerShouldFail("[1") { readListByElement { skipValue() } }
			readerShouldFail("[1 1]") { readListByElement { skipValue() } }
			readerShouldFail("[,1,1]") { readListByElement { skipValue() } }
			readerShouldFail("[1,,1]") { readListByElement { skipValue() } }
			readerShouldFail("[1,1,]") { readListByElement { skipValue() } }
			readerShouldFail("[[1]") { readListByElement { skipValue() } }
			readerShouldFail("null") { readListByElement { skipValue() } }
		}

		it("readListEnd()") {
			readerShouldFail("") { readListEnd() }
			readerShouldFail("{") { readListEnd() }
			readerShouldFail("[") { readListEnd() }
			readerShouldFail("]") { readListEnd() }
			readerShouldFail("}") { readListEnd() }
			readerShouldFail("[}") { readListStart(); readListEnd() }
			readerShouldFail("null") { readListEnd() }
		}

		it("readListOrNull()") {
			readerShouldFail("") { readListOrNull() }
			readerShouldFail("[") { readListOrNull() }
			readerShouldFail("[1") { readListOrNull() }
			readerShouldFail("[1 1]") { readListOrNull() }
			readerShouldFail("[,1,1]") { readListOrNull() }
			readerShouldFail("[1,,1]") { readListOrNull() }
			readerShouldFail("[1,1,]") { readListOrNull() }
			readerShouldFail("[[1]") { readListOrNull() }
		}

		it("readListStart()") {
			readerShouldFail("") { readListStart() }
			readerShouldFail("{") { readListStart() }
			readerShouldFail("]") { readListStart() }
			readerShouldFail("null") { readListStart() }
		}

		it("readLong()") {
			readerShouldFail("") { readLong() }
			readerShouldFail("0b0") { readLong() }
			readerShouldFail("0o0") { readLong() }
			readerShouldFail("0x0") { readLong() }
			readerShouldFail("01") { readLong() }
			readerShouldFail("01.0") { readLong() }
			readerShouldFail("+0") { readLong() }
			readerShouldFail("+1") { readLong() }
			readerShouldFail("-+1") { readLong() }
			readerShouldFail("-true") { readLong() }
			readerShouldFail("-.") { readLong() }
			readerShouldFail("-e") { readLong() }
			readerShouldFail("0.") { readLong() }
			readerShouldFail("0e") { readLong() }
			readerShouldFail("1.") { readLong() }
			readerShouldFail("1.e") { readLong() }
			readerShouldFail("1e") { readLong() }
			readerShouldFail("1ee") { readLong() }
			readerShouldFail("1e.") { readLong() }
			readerShouldFail("1e+") { readLong() }
			readerShouldFail("1e+e") { readLong() }
			readerShouldFail("1e-") { readLong() }
			readerShouldFail("1e-e") { readLong() }
			readerShouldFail("null") { readLong() }
		}

		it("readLongOrNull()") {
			readerShouldFail("") { readLongOrNull() }
			readerShouldFail("0b0") { readLongOrNull() }
			readerShouldFail("0o0") { readLongOrNull() }
			readerShouldFail("0x0") { readLongOrNull() }
			readerShouldFail("01") { readLongOrNull() }
			readerShouldFail("01.0") { readLongOrNull() }
			readerShouldFail("+0") { readLongOrNull() }
			readerShouldFail("+1") { readLongOrNull() }
			readerShouldFail("-+1") { readLongOrNull() }
			readerShouldFail("-true") { readLongOrNull() }
			readerShouldFail("-.") { readLongOrNull() }
			readerShouldFail("-e") { readLongOrNull() }
			readerShouldFail("0.") { readLongOrNull() }
			readerShouldFail("0e") { readLongOrNull() }
			readerShouldFail("1.") { readLongOrNull() }
			readerShouldFail("1.e") { readLongOrNull() }
			readerShouldFail("1e") { readLongOrNull() }
			readerShouldFail("1ee") { readLongOrNull() }
			readerShouldFail("1e.") { readLongOrNull() }
			readerShouldFail("1e+") { readLongOrNull() }
			readerShouldFail("1e+e") { readLongOrNull() }
			readerShouldFail("1e-") { readLongOrNull() }
			readerShouldFail("1e-e") { readLongOrNull() }
		}

		it("readMap()") {
			readerShouldFail("") { readMap() }
			readerShouldFail("{") { readMap() }
			readerShouldFail("{\"") { readMap() }
			readerShouldFail("{\"x") { readMap() }
			readerShouldFail("{\"x\"") { readMap() }
			readerShouldFail("{\"x\":") { readMap() }
			readerShouldFail("{\"x\":1") { readMap() }
			readerShouldFail("{\"x\":1,") { readMap() }
			readerShouldFail("{true \"key\": 1}") { readMap() }
			readerShouldFail("{\"key\" true: 1}") { readMap() }
			readerShouldFail("{\"key\": 1 true}") { readMap() }
			readerShouldFail("{\"key\"::1}") { readMap() }
			readerShouldFail("{\"key\":1:}") { readMap() }
			readerShouldFail("{\"key\":1,}") { readMap() }
			readerShouldFail("{,\"key\":1}") { readMap() }
			readerShouldFail("{\"key0\":0,,\"key1\":1}") { readMap() }
			readerShouldFail("null") { readMap() }
		}

		it("readMapEnd()") {
			readerShouldFail("") { readMapEnd() }
			readerShouldFail("[") { readMapEnd() }
			readerShouldFail("{") { readMapEnd() }
			readerShouldFail("}") { readMapEnd() }
			readerShouldFail("]") { readMapEnd() }
			readerShouldFail("{]") { readMapStart(); readMapEnd() }
			readerShouldFail("null") { readMapEnd() }
		}

		it("readMapKey()") {
			readerShouldFail("") { readMapKey() }
			readerShouldFail("\"test") { readMapKey() }
			readerShouldFail("\"\\a\"") { readMapKey() }
			readerShouldFail("\"\\0\"") { readMapKey() }
			readerShouldFail("\"\\,\"") { readMapKey() }
			readerShouldFail("\"\\:\"") { readMapKey() }
			readerShouldFail("\"\\[\"") { readMapKey() }
			readerShouldFail("\"\\u000Z\"") { readMapKey() }
			readerShouldFail("\"\\u00ZZ\"") { readMapKey() }
			readerShouldFail("\"\\u0ZZZ\"") { readMapKey() }
			readerShouldFail("\"\\uZZZZ\"") { readMapKey() }
			readerShouldFail("null") { readMapKey() }
		}

		it("readMapOrNull()") {
			readerShouldFail("") { readMapOrNull() }
			readerShouldFail("{") { readMapOrNull() }
			readerShouldFail("{\"") { readMapOrNull() }
			readerShouldFail("{\"x") { readMapOrNull() }
			readerShouldFail("{\"x\"") { readMapOrNull() }
			readerShouldFail("{\"x\":") { readMapOrNull() }
			readerShouldFail("{\"x\":1") { readMapOrNull() }
			readerShouldFail("{\"x\":1,") { readMapOrNull() }
			readerShouldFail("{true \"key\": 1}") { readMapOrNull() }
			readerShouldFail("{\"key\" true: 1}") { readMapOrNull() }
			readerShouldFail("{\"key\": 1 true}") { readMapOrNull() }
			readerShouldFail("{\"key\"::1}") { readMapOrNull() }
			readerShouldFail("{\"key\":1:}") { readMapOrNull() }
			readerShouldFail("{\"key\":1,}") { readMapOrNull() }
			readerShouldFail("{,\"key\":1}") { readMapOrNull() }
			readerShouldFail("{\"key0\":0,,\"key1\":1}") { readMapOrNull() }
		}

		it("readMapStart()") {
			readerShouldFail("") { readMapStart() }
			readerShouldFail("[") { readMapStart() }
			readerShouldFail("}") { readMapStart() }
			readerShouldFail("null") { readMapStart() }
		}

		it("readString()") {
			readerShouldFail("") { readString() }
			readerShouldFail("\"test") { readString() }
			readerShouldFail("\"\\a\"") { readString() }
			readerShouldFail("\"\\0\"") { readString() }
			readerShouldFail("\"\\,\"") { readString() }
			readerShouldFail("\"\\:\"") { readString() }
			readerShouldFail("\"\\[\"") { readString() }
			readerShouldFail("\"\\u000Z\"") { readString() }
			readerShouldFail("\"\\u00ZZ\"") { readString() }
			readerShouldFail("\"\\u0ZZZ\"") { readString() }
			readerShouldFail("\"\\uZZZZ\"") { readString() }
			readerShouldFail("null") { readString() }

			listOf(
				'\u0000', '\u0001', '\u0002', '\u0003', '\u0004', '\u0005', '\u0006', '\u0007',
				'\u000B', '\u000C', '\u000E', '\u000F', '\u0010', '\u0011', '\u0012', '\u0013',
				'\u0014', '\u0015', '\u0016', '\u0017', '\u0018', '\u0019', '\u001A', '\u001B',
				'\u001C', '\u001D', '\u001E', '\u001F', '\b', '\n', '\r', '\t'
			)
				.forEach { readerShouldFail("\"$it\"") { readString() } }
		}

		it("readStringOrNull()") {
			readerShouldFail("") { readStringOrNull() }
			readerShouldFail("\"test") { readStringOrNull() }
			readerShouldFail("\"\\a\"") { readStringOrNull() }
			readerShouldFail("\"\\0\"") { readStringOrNull() }
			readerShouldFail("\"\\,\"") { readStringOrNull() }
			readerShouldFail("\"\\:\"") { readStringOrNull() }
			readerShouldFail("\"\\[\"") { readStringOrNull() }
			readerShouldFail("\"\\u000Z\"") { readStringOrNull() }
			readerShouldFail("\"\\u00ZZ\"") { readStringOrNull() }
			readerShouldFail("\"\\u0ZZZ\"") { readStringOrNull() }
			readerShouldFail("\"\\uZZZZ\"") { readStringOrNull() }

			listOf(
				'\u0000', '\u0001', '\u0002', '\u0003', '\u0004', '\u0005', '\u0006', '\u0007',
				'\u000B', '\u000C', '\u000E', '\u000F', '\u0010', '\u0011', '\u0012', '\u0013',
				'\u0014', '\u0015', '\u0016', '\u0017', '\u0018', '\u0019', '\u001A', '\u001B',
				'\u001C', '\u001D', '\u001E', '\u001F', '\b', '\n', '\r', '\t'
			)
				.forEach { readerShouldFail("\"$it\"") { readStringOrNull() } }
		}

		it("readNull()") {
			readerShouldFail("") { readNull() }
			readerShouldFail("true") { readNull() }
			readerShouldFail("false") { readNull() }
			readerShouldFail("0") { readNull() }
			readerShouldFail("\"\"") { readNull() }
			readerShouldFail("[]") { readNull() }
			readerShouldFail("{}") { readNull() }
		}

		it("readNumber()") {
			readerShouldFail("") { readNumber() }
			readerShouldFail("0b0") { readNumber() }
			readerShouldFail("0o0") { readNumber() }
			readerShouldFail("0x0") { readNumber() }
			readerShouldFail("01") { readNumber() }
			readerShouldFail("01.0") { readNumber() }
			readerShouldFail("+0") { readNumber() }
			readerShouldFail("+1") { readNumber() }
			readerShouldFail("-+1") { readNumber() }
			readerShouldFail("-true") { readNumber() }
			readerShouldFail("-.") { readNumber() }
			readerShouldFail("-e") { readNumber() }
			readerShouldFail("0.") { readNumber() }
			readerShouldFail("0e") { readNumber() }
			readerShouldFail("1.") { readNumber() }
			readerShouldFail("1.e") { readNumber() }
			readerShouldFail("1e") { readNumber() }
			readerShouldFail("1ee") { readNumber() }
			readerShouldFail("1e.") { readNumber() }
			readerShouldFail("1e+") { readNumber() }
			readerShouldFail("1e+e") { readNumber() }
			readerShouldFail("1e-") { readNumber() }
			readerShouldFail("1e-e") { readNumber() }
			readerShouldFail("null") { readNumber() }
		}

		it("readNumberOrNull()") {
			readerShouldFail("") { readNumberOrNull() }
			readerShouldFail("0b0") { readNumberOrNull() }
			readerShouldFail("0o0") { readNumberOrNull() }
			readerShouldFail("0x0") { readNumberOrNull() }
			readerShouldFail("01") { readNumberOrNull() }
			readerShouldFail("01.0") { readNumberOrNull() }
			readerShouldFail("+0") { readNumberOrNull() }
			readerShouldFail("+1") { readNumberOrNull() }
			readerShouldFail("-+1") { readNumberOrNull() }
			readerShouldFail("-true") { readNumberOrNull() }
			readerShouldFail("-.") { readNumberOrNull() }
			readerShouldFail("-e") { readNumberOrNull() }
			readerShouldFail("0.") { readNumberOrNull() }
			readerShouldFail("0e") { readNumberOrNull() }
			readerShouldFail("1.") { readNumberOrNull() }
			readerShouldFail("1.e") { readNumberOrNull() }
			readerShouldFail("1e") { readNumberOrNull() }
			readerShouldFail("1ee") { readNumberOrNull() }
			readerShouldFail("1e.") { readNumberOrNull() }
			readerShouldFail("1e+") { readNumberOrNull() }
			readerShouldFail("1e+e") { readNumberOrNull() }
			readerShouldFail("1e-") { readNumberOrNull() }
			readerShouldFail("1e-e") { readNumberOrNull() }
		}

		it("skipValue()") {
			readerShouldFail("") { skipValue() }
			readerShouldFail("[") { skipValue() }
			readerShouldFail("]") { skipValue() }
			readerShouldFail("null") { skipValue(); skipValue() }
		}
	}
})


// TODO move the following methods inside the object above once KT-19796 is fixed
// https://youtrack.jetbrains.com/issue/KT-19796

@Suppress("unused")
private inline fun TestBody.readerShouldFail(string: String, body: JSONReader.() -> Unit) {
	try {
		StandardReader(TextInput(StringReader(string))).body()
		throw AssertionError("should fail with a JSONException")
	}
	catch (e: JSONException) {
		// good
	}
}
