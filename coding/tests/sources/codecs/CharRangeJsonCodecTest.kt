package tests.coding

import ch.tutteli.atrium.api.fluent.en_GB.*
import ch.tutteli.atrium.api.verbs.*
import io.fluidsonic.json.*
import org.junit.jupiter.api.*


@Suppress("UNCHECKED_CAST")
internal object CharRangeJsonCodecTest {

	val codecs = JsonCodecProvider(
		CharJsonCodec,
		CharRangeJsonCodec
	)

	val parser = JsonCodingParser.builder()
		.decodingWith(codecs, base = null)
		.build()

	val serializer = JsonCodingSerializer.builder()
		.encodingWith(codecs, base = null)
		.build()


	@Test
	fun testDecodesCharRange() {
		expect(parser.parseValueOfType<CharRange>("""{"start":"\u0000","endInclusive":"a"}"""))
			.toBe(CharRange(0.toChar(), 'a'))
	}


	@Test
	fun testEncodesCharRange() {
		expect(serializer.serializeValue(CharRange(0.toChar(), 'a')))
			.toBe("""{"start":"\u0000","endInclusive":"a"}""")
	}
}
