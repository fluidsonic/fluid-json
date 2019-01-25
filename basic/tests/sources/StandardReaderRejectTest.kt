package tests.basic

import com.github.fluidsonic.fluid.json.*
import org.junit.jupiter.api.Test
import java.io.StringReader


internal object StandardReaderRejectTest {

	@Test
	fun testNextToken() {
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
		readerShouldFail("null") { close(); nextToken }
	}


	@Test
	fun testReadBoolean() {
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
		readerShouldFail("true") { readBoolean(); readBoolean() }
		readerShouldFail("true") { close(); readBoolean() }
	}


	@Test
	fun testReadBooleanOrNull() {
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
		readerShouldFail("true") { readBooleanOrNull(); readBooleanOrNull() }
		readerShouldFail("true") { close(); readBooleanOrNull() }
	}


	@Test
	fun testReadByte() {
		readerShouldFail("") { readByte() }
		readerShouldFail("0b0") { readByte() }
		readerShouldFail("0o0") { readByte() }
		readerShouldFail("0x0") { readByte() }
		readerShouldFail("01") { readByte() }
		readerShouldFail("01.0") { readByte() }
		readerShouldFail("+0") { readByte() }
		readerShouldFail("+1") { readByte() }
		readerShouldFail("-+1") { readByte() }
		readerShouldFail("-true") { readByte() }
		readerShouldFail("-.") { readByte() }
		readerShouldFail("-e") { readByte() }
		readerShouldFail("0.") { readByte() }
		readerShouldFail("0e") { readByte() }
		readerShouldFail("1.") { readByte() }
		readerShouldFail("1.e") { readByte() }
		readerShouldFail("1e") { readByte() }
		readerShouldFail("1ee") { readByte() }
		readerShouldFail("1e.") { readByte() }
		readerShouldFail("1e+") { readByte() }
		readerShouldFail("1e+e") { readByte() }
		readerShouldFail("1e-") { readByte() }
		readerShouldFail("1e-e") { readByte() }
		readerShouldFail("null") { readByte() }
		readerShouldFail("0") { readByte(); readByte() }
		readerShouldFail("0") { close(); readByte() }
	}


	@Test
	fun testReadByteOrNull() {
		readerShouldFail("") { readByteOrNull() }
		readerShouldFail("0b0") { readByteOrNull() }
		readerShouldFail("0o0") { readByteOrNull() }
		readerShouldFail("0x0") { readByteOrNull() }
		readerShouldFail("01") { readByteOrNull() }
		readerShouldFail("01.0") { readByteOrNull() }
		readerShouldFail("+0") { readByteOrNull() }
		readerShouldFail("+1") { readByteOrNull() }
		readerShouldFail("-+1") { readByteOrNull() }
		readerShouldFail("-true") { readByteOrNull() }
		readerShouldFail("-.") { readByteOrNull() }
		readerShouldFail("-e") { readByteOrNull() }
		readerShouldFail("0.") { readByteOrNull() }
		readerShouldFail("0e") { readByteOrNull() }
		readerShouldFail("1.") { readByteOrNull() }
		readerShouldFail("1.e") { readByteOrNull() }
		readerShouldFail("1e") { readByteOrNull() }
		readerShouldFail("1ee") { readByteOrNull() }
		readerShouldFail("1e.") { readByteOrNull() }
		readerShouldFail("1e+") { readByteOrNull() }
		readerShouldFail("1e+e") { readByteOrNull() }
		readerShouldFail("1e-") { readByteOrNull() }
		readerShouldFail("1e-e") { readByteOrNull() }
		readerShouldFail("0") { readByteOrNull(); readByteOrNull() }
		readerShouldFail("0") { close(); readByteOrNull() }
	}


	@Test
	fun testReadDouble() {
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
		readerShouldFail("0") { readDouble(); readDouble() }
		readerShouldFail("0") { close(); readDouble() }
	}


	@Test
	fun testReadDoubleOrNull() {
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
		readerShouldFail("0") { readDoubleOrNull(); readDoubleOrNull() }
		readerShouldFail("0") { close(); readDoubleOrNull() }
	}


	@Test
	fun testReadFloat() {
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
		readerShouldFail("0") { readFloat(); readFloat() }
		readerShouldFail("0") { close(); readFloat() }
	}


	@Test
	fun testReadFloatOrNull() {
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
		readerShouldFail("0") { readFloatOrNull(); readFloatOrNull() }
		readerShouldFail("0") { close(); readFloatOrNull() }
	}


	@Test
	fun testReadFromList() {
		readerShouldFail("") { readFromList {} }
		readerShouldFail("[") { readFromList {} }
		readerShouldFail("[1") { readFromList {} }
		readerShouldFail("[1 1]") { readFromList {} }
		readerShouldFail("[,1,1]") { readFromList {} }
		readerShouldFail("[1,,1]") { readFromList {} }
		readerShouldFail("[1,1,]") { readFromList {} }
		readerShouldFail("[[1]") { readFromList {} }
		readerShouldFail("null") { readFromList {} }
		readerShouldFail("[]") { readFromList {}; readFromList {} }
		readerShouldFail("[]") { close(); readFromList {} }
	}


	@Test
	fun testReadFromMapByElementValue() {
		readerShouldFail("") { readFromMapByElementValue { skipValue() } }
		readerShouldFail("{") { readFromMapByElementValue { skipValue() } }
		readerShouldFail("{\"") { readFromMapByElementValue { skipValue() } }
		readerShouldFail("{\"x") { readFromMapByElementValue { skipValue() } }
		readerShouldFail("{\"x\"") { readFromMapByElementValue { skipValue() } }
		readerShouldFail("{\"x\":") { readFromMapByElementValue { skipValue() } }
		readerShouldFail("{\"x\":1") { readFromMapByElementValue { skipValue() } }
		readerShouldFail("{\"x\":1,") { readFromMapByElementValue { skipValue() } }
		readerShouldFail("{true \"key\": 1}") { readFromMapByElementValue { skipValue() } }
		readerShouldFail("{\"key\" true: 1}") { readFromMapByElementValue { skipValue() } }
		readerShouldFail("{\"key\": 1 true}") { readFromMapByElementValue { skipValue() } }
		readerShouldFail("{\"key\"::1}") { readFromMapByElementValue { skipValue() } }
		readerShouldFail("{\"key\":1:}") { readFromMapByElementValue { skipValue() } }
		readerShouldFail("{\"key\":1,}") { readFromMapByElementValue { skipValue() } }
		readerShouldFail("{,\"key\":1}") { readFromMapByElementValue { skipValue() } }
		readerShouldFail("{\"key0\":0,,\"key1\":1}") { readFromMapByElementValue { skipValue() } }
		readerShouldFail("null") { readFromMapByElementValue { skipValue() } }
		readerShouldFail("{}") { readFromMapByElementValue { skipValue() }; readFromMapByElementValue { skipValue() } }
		readerShouldFail("{}") { close(); readFromMapByElementValue { skipValue() } }
	}


	@Test
	fun testReadInt() {
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
		readerShouldFail("0") { readInt(); readInt() }
		readerShouldFail("0") { close(); readInt() }
	}


	@Test
	fun testReadIntOrNull() {
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
		readerShouldFail("0") { readIntOrNull(); readIntOrNull() }
		readerShouldFail("0") { close(); readIntOrNull() }
	}


	@Test
	fun testReadList() {
		readerShouldFail("") { readList() }
		readerShouldFail("[") { readList() }
		readerShouldFail("[1") { readList() }
		readerShouldFail("[1 1]") { readList() }
		readerShouldFail("[,1,1]") { readList() }
		readerShouldFail("[1,,1]") { readList() }
		readerShouldFail("[1,1,]") { readList() }
		readerShouldFail("[[1]") { readList() }
		readerShouldFail("null") { readList() }
		readerShouldFail("[]") { readList(); readList() }
		readerShouldFail("[]") { close(); readList() }
	}


	@Test
	fun testReadListByElement() {
		readerShouldFail("") { readListByElement { skipValue() } }
		readerShouldFail("[") { readListByElement { skipValue() } }
		readerShouldFail("[1") { readListByElement { skipValue() } }
		readerShouldFail("[1 1]") { readListByElement { skipValue() } }
		readerShouldFail("[,1,1]") { readListByElement { skipValue() } }
		readerShouldFail("[1,,1]") { readListByElement { skipValue() } }
		readerShouldFail("[1,1,]") { readListByElement { skipValue() } }
		readerShouldFail("[[1]") { readListByElement { skipValue() } }
		readerShouldFail("null") { readListByElement { skipValue() } }
		readerShouldFail("[]") { readListByElement { skipValue() }; readListByElement { skipValue() } }
		readerShouldFail("[]") { close(); readListByElement { skipValue() } }
	}


	@Test
	fun testReadListEnd() {
		readerShouldFail("") { readListEnd() }
		readerShouldFail("{") { readListEnd() }
		readerShouldFail("[") { readListEnd() }
		readerShouldFail("]") { readListEnd() }
		readerShouldFail("}") { readListEnd() }
		readerShouldFail("[}") { readListStart(); readListEnd() }
		readerShouldFail("null") { readListEnd() }
		readerShouldFail("[]") { readListStart(); readListEnd(); readListEnd() }
		readerShouldFail("[]") { readListStart(); close(); readListEnd() }
	}


	@Test
	fun testReadListOrNull() {
		readerShouldFail("") { readListOrNull() }
		readerShouldFail("[") { readListOrNull() }
		readerShouldFail("[1") { readListOrNull() }
		readerShouldFail("[1 1]") { readListOrNull() }
		readerShouldFail("[,1,1]") { readListOrNull() }
		readerShouldFail("[1,,1]") { readListOrNull() }
		readerShouldFail("[1,1,]") { readListOrNull() }
		readerShouldFail("[[1]") { readListOrNull() }
		readerShouldFail("[]") { readListOrNull(); readListOrNull() }
		readerShouldFail("[]") { close(); readListOrNull() }
	}


	@Test
	fun testReadListStart() {
		readerShouldFail("") { readListStart() }
		readerShouldFail("{") { readListStart() }
		readerShouldFail("]") { readListStart() }
		readerShouldFail("null") { readListStart() }
		readerShouldFail("[") { readListStart(); readListStart() }
		readerShouldFail("[") { close(); readListStart() }
	}


	@Test
	fun testReadLong() {
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
		readerShouldFail("0") { readLong(); readLong() }
		readerShouldFail("0") { close(); readLong() }
	}


	@Test
	fun testReadLongOrNull() {
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
		readerShouldFail("0") { readLongOrNull(); readLongOrNull() }
		readerShouldFail("0") { close(); readLongOrNull() }
	}


	@Test
	fun testReadMap() {
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
		readerShouldFail("{}") { readMap(); readMap() }
		readerShouldFail("{}") { close(); readMap() }
	}


	@Test
	fun testReadMapEnd() {
		readerShouldFail("") { readMapEnd() }
		readerShouldFail("[") { readMapEnd() }
		readerShouldFail("{") { readMapEnd() }
		readerShouldFail("}") { readMapEnd() }
		readerShouldFail("]") { readMapEnd() }
		readerShouldFail("{]") { readMapStart(); readMapEnd() }
		readerShouldFail("null") { readMapEnd() }
		readerShouldFail("{}") { readMapStart(); readMapEnd(); readMapEnd() }
		readerShouldFail("{}") { readMapStart(); close(); readMapEnd() }
	}

	@Test
	fun testReadMapKey() {
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
		readerShouldFail("\"\"") { readMapKey(); readMapKey() }
		readerShouldFail("\"\"") { close(); readMapKey() }
	}


	@Test
	fun testReadMapOrNull() {
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
		readerShouldFail("{}") { readMapOrNull(); readMapOrNull() }
		readerShouldFail("{}") { close(); readMapOrNull() }
	}


	@Test
	fun testReadMapStart() {
		readerShouldFail("") { readMapStart() }
		readerShouldFail("[") { readMapStart() }
		readerShouldFail("}") { readMapStart() }
		readerShouldFail("null") { readMapStart() }
		readerShouldFail("{") { readMapStart(); readMapStart() }
		readerShouldFail("{") { close(); readMapStart() }
	}


	@Test
	fun testReadString() {
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
		readerShouldFail("\"\"") { readString(); readString() }
		readerShouldFail("\"\"") { close(); readString() }

		listOf(
			'\u0000', '\u0001', '\u0002', '\u0003', '\u0004', '\u0005', '\u0006', '\u0007',
			'\u000B', '\u000C', '\u000E', '\u000F', '\u0010', '\u0011', '\u0012', '\u0013',
			'\u0014', '\u0015', '\u0016', '\u0017', '\u0018', '\u0019', '\u001A', '\u001B',
			'\u001C', '\u001D', '\u001E', '\u001F', '\b', '\n', '\r', '\t'
		)
			.forEach { readerShouldFail("\"$it\"") { readString() } }
	}


	@Test
	fun testReadStringOrNull() {
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
		readerShouldFail("\"\"") { readStringOrNull(); readStringOrNull() }
		readerShouldFail("\"\"") { close(); readStringOrNull() }

		listOf(
			'\u0000', '\u0001', '\u0002', '\u0003', '\u0004', '\u0005', '\u0006', '\u0007',
			'\u000B', '\u000C', '\u000E', '\u000F', '\u0010', '\u0011', '\u0012', '\u0013',
			'\u0014', '\u0015', '\u0016', '\u0017', '\u0018', '\u0019', '\u001A', '\u001B',
			'\u001C', '\u001D', '\u001E', '\u001F', '\b', '\n', '\r', '\t'
		)
			.forEach { readerShouldFail("\"$it\"") { readStringOrNull() } }
	}


	@Test
	fun testReadNull() {
		readerShouldFail("") { readNull() }
		readerShouldFail("true") { readNull() }
		readerShouldFail("false") { readNull() }
		readerShouldFail("0") { readNull() }
		readerShouldFail("\"\"") { readNull() }
		readerShouldFail("[]") { readNull() }
		readerShouldFail("{}") { readNull() }
		readerShouldFail("null") { readNull(); readNull() }
		readerShouldFail("null") { close(); readNull() }
	}


	@Test
	fun testReadNumber() {
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
		readerShouldFail("0") { readNumber(); readNumber() }
		readerShouldFail("0") { close(); readNumber() }
	}


	@Test
	fun testReadNumberOrNull() {
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
		readerShouldFail("0") { readNumberOrNull(); readNumberOrNull() }
		readerShouldFail("0") { close(); readNumberOrNull() }
	}


	@Test
	fun testReadShort() {
		readerShouldFail("") { readShort() }
		readerShouldFail("0b0") { readShort() }
		readerShouldFail("0o0") { readShort() }
		readerShouldFail("0x0") { readShort() }
		readerShouldFail("01") { readShort() }
		readerShouldFail("01.0") { readShort() }
		readerShouldFail("+0") { readShort() }
		readerShouldFail("+1") { readShort() }
		readerShouldFail("-+1") { readShort() }
		readerShouldFail("-true") { readShort() }
		readerShouldFail("-.") { readShort() }
		readerShouldFail("-e") { readShort() }
		readerShouldFail("0.") { readShort() }
		readerShouldFail("0e") { readShort() }
		readerShouldFail("1.") { readShort() }
		readerShouldFail("1.e") { readShort() }
		readerShouldFail("1e") { readShort() }
		readerShouldFail("1ee") { readShort() }
		readerShouldFail("1e.") { readShort() }
		readerShouldFail("1e+") { readShort() }
		readerShouldFail("1e+e") { readShort() }
		readerShouldFail("1e-") { readShort() }
		readerShouldFail("1e-e") { readShort() }
		readerShouldFail("null") { readShort() }
		readerShouldFail("0") { readShort(); readShort() }
		readerShouldFail("0") { close(); readShort() }
	}


	@Test
	fun testReadShortOrNull() {
		readerShouldFail("") { readShortOrNull() }
		readerShouldFail("0b0") { readShortOrNull() }
		readerShouldFail("0o0") { readShortOrNull() }
		readerShouldFail("0x0") { readShortOrNull() }
		readerShouldFail("01") { readShortOrNull() }
		readerShouldFail("01.0") { readShortOrNull() }
		readerShouldFail("+0") { readShortOrNull() }
		readerShouldFail("+1") { readShortOrNull() }
		readerShouldFail("-+1") { readShortOrNull() }
		readerShouldFail("-true") { readShortOrNull() }
		readerShouldFail("-.") { readShortOrNull() }
		readerShouldFail("-e") { readShortOrNull() }
		readerShouldFail("0.") { readShortOrNull() }
		readerShouldFail("0e") { readShortOrNull() }
		readerShouldFail("1.") { readShortOrNull() }
		readerShouldFail("1.e") { readShortOrNull() }
		readerShouldFail("1e") { readShortOrNull() }
		readerShouldFail("1ee") { readShortOrNull() }
		readerShouldFail("1e.") { readShortOrNull() }
		readerShouldFail("1e+") { readShortOrNull() }
		readerShouldFail("1e+e") { readShortOrNull() }
		readerShouldFail("1e-") { readShortOrNull() }
		readerShouldFail("1e-e") { readShortOrNull() }
		readerShouldFail("0") { readShortOrNull(); readShortOrNull() }
		readerShouldFail("0") { close(); readShortOrNull() }
	}


	@Test
	fun testReadValue() {
		readerShouldFail("") { readValue() }
		readerShouldFail("true") { skipValue(); readValue() }
		readerShouldFail("{") { readValue() }
		readerShouldFail("[") { readValue() }
		readerShouldFail("{}") { readMapStart(); readValue() }
		readerShouldFail("[]") { readListStart();readValue() }
		readerShouldFail("0") { readValue(); readValue() }
		readerShouldFail("0") { close(); readValue() }
	}


	@Test
	fun testReadValueOrNull() {
		readerShouldFail("") { readValueOrNull() }
		readerShouldFail("true") { skipValue(); readValueOrNull() }
		readerShouldFail("{") { readValueOrNull() }
		readerShouldFail("[") { readValueOrNull() }
		readerShouldFail("{}") { readMapStart(); readValueOrNull() }
		readerShouldFail("[]") { readListStart();readValueOrNull() }
		readerShouldFail("0") { readValueOrNull(); readValueOrNull() }
		readerShouldFail("0") { close(); readValueOrNull() }
	}


	@Test
	fun testSkipValue() {
		readerShouldFail("") { skipValue() }
		readerShouldFail("[") { skipValue() }
		readerShouldFail("]") { skipValue() }
		readerShouldFail("null") { skipValue(); skipValue() }
		readerShouldFail("0") { skipValue(); skipValue() }
		readerShouldFail("0") { close(); skipValue() }
	}


	@Test
	fun testTerminate() {
		readerShouldFail("null") { terminate() }
	}


	private inline fun readerShouldFail(string: String, block: JSONReader.() -> Unit) {
		try {
			StandardReader(TextInput(StringReader(string))).block()
			throw AssertionError("should fail with a JSONException for JSON: $string")
		}
		catch (e: JSONException) {
			// good
		}
		catch (e: Throwable) {
			throw AssertionError("should fail with a JSONException for JSON: $string", e)
		}
	}
}
