package tests.coding

import io.fluidsonic.json.*
import org.junit.jupiter.api.*


internal object StandardCodingParserRejectTest {

	@Test
	fun testUnknownConstants() {
		failToParse("void")
	}


	@Test
	fun testPartialConstants() {
		failToParse("tru")
		failToParse("fals")
		failToParse("nul")
	}


	@Test
	fun testExtraDataAfterConstants() {
		failToParse("true2")
		failToParse("false2")
		failToParse("null2")
		failToParse("null\"")
	}


	@Test
	fun testMultipleValue() {
		failToParse("1 2")
		failToParse("1, 2")
		failToParse("true false")
		failToParse("null null")
	}


	@Test
	fun testNonDecimalNumberFormat() {
		failToParse("0b0")
		failToParse("0o0")
		failToParse("0x0")
	}


	@Test
	fun testNonZeroNumbersWithLeadingZero() {
		failToParse("01")
		failToParse("01.0")
	}


	@Test
	fun testPositiveNumberSign() {
		failToParse("+0")
		failToParse("+1")
		failToParse("+-1")
	}


	@Test
	fun testIncompleteOrWeirdNumbers() {
		failToParse("-true")
		failToParse("-.")
		failToParse("-e")
		failToParse("0.")
		failToParse("0e")
		failToParse("1.")
		failToParse("1.e")
		failToParse("1e")
		failToParse("1ee")
		failToParse("1e.")
		failToParse("1e+")
		failToParse("1e+e")
		failToParse("1e-")
		failToParse("1e-e")
	}


	@Test
	fun testExtraDataAfterNumber() {
		failToParse("0.0z")
		failToParse("0.0\"")
	}


	@Test
	fun testUnterminatedString() {
		failToParse("\"test")
		failToParse("\"test\\uDD\"")
		failToParse("{\"test")
		failToParse("{\"key\":\"test")
	}


	@Test
	fun testUnknownEscapeSeqence() {
		failToParse("\"\\a\"")
		failToParse("\"\\0\"")
		failToParse("\"\\,\"")
		failToParse("\"\\:\"")
		failToParse("\"\\[\"")
	}


	@Test
	fun testInvalidUnicodeEscapeSequences() {
		failToParse("\"\\u000Z\"")
		failToParse("\"\\u00ZZ\"")
		failToParse("\"\\u0ZZZ\"")
		failToParse("\"\\uZZZZ\"")
	}


	@Test
	fun testUnescapedControlCharacters() {
		listOf(
			'\u0000', '\u0001', '\u0002', '\u0003', '\u0004', '\u0005', '\u0006', '\u0007',
			'\u000B', '\u000C', '\u000E', '\u000F', '\u0010', '\u0011', '\u0012', '\u0013',
			'\u0014', '\u0015', '\u0016', '\u0017', '\u0018', '\u0019', '\u001A', '\u001B',
			'\u001C', '\u001D', '\u001E', '\u001F', '\b', '\n', '\r', '\t'
		)
			.forEach { failToParse("\"$it\"") }
	}


	@Test
	fun testUnterminatedList() {
		failToParse("[")
		failToParse("[1")
		failToParse("[1,")
	}


	@Test
	fun testExtraDataAfterList() {
		failToParse("[1]b")
	}


	@Test
	fun testExtraDataAfterListElement() {
		failToParse("[1 1]")
	}


	@Test
	fun testMisplacedCommaInList() {
		failToParse("[,1,1]")
		failToParse("[1,,1]")
		failToParse("[1,1,]")
	}


	@Test
	fun testUnterminatedMap() {
		failToParse("{")
		failToParse("{\"")
		failToParse("{\"x")
		failToParse("{\"x\"")
		failToParse("{\"x\":")
		failToParse("{\"x\":1")
		failToParse("{\"x\":1,")
	}


	@Test
	fun testExtraDataAfterMap() {
		failToParse("{\"key\": 1}b")
	}


	@Test
	fun testExtraDataBeforeMapKeu() {
		failToParse("{true \"key\": 1}")
	}


	@Test
	fun testExtraDataAfterMapKey() {
		failToParse("{\"key\" true: 1}")
	}


	@Test
	fun testExtraDataBeforeMapValue() {
		failToParse("{\"key\"::1}")
	}


	@Test
	fun testExtraDataAfterMapValue() {
		failToParse("{\"key\":1:}")
		failToParse("{\"key\":1 \"key\"}")
	}


	@Test
	fun testMisplacedCommasInMaps() {
		failToParse("{,\"key0\":1,\"key1\":1}")
		failToParse("{\"key0\":1,,\"key1\":1}")
		failToParse("{\"key0\":1,\"key1\":1,}")
	}


	private inline fun shouldFailWithJsonException(block: () -> Unit) {
		try {
			block()
			throw AssertionError("should fail with a JsonException")
		}
		catch (e: JsonException) {
			// good
		}
	}


	private fun failToParse(string: String) {
		val parser = StandardCodingParser(JsonCodingContext.empty) { source, context ->
			JsonDecoder.builder(context)
				.codecs()
				.source(source)
				.build()
		}

		shouldFailWithJsonException { parser.parseValue(string) }
	}
}
