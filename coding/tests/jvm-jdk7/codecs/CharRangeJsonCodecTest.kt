package tests.coding

import io.fluidsonic.json.*
import kotlin.test.*


@Suppress("UNCHECKED_CAST")
class CharRangeJsonCodecTest {

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
