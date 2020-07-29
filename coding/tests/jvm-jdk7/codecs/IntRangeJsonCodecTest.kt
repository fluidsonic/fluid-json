package tests.coding

import io.fluidsonic.json.*
import kotlin.test.*


@Suppress("UNCHECKED_CAST")
class IntRangeJsonCodecTest {

	val codecs = JsonCodecProvider(
		IntJsonCodec,
		IntRangeJsonCodec
	)

	val parser = JsonCodingParser.builder()
		.decodingWith(codecs, base = null)
		.build()

	val serializer = JsonCodingSerializer.builder()
		.encodingWith(codecs, base = null)
		.build()


	@Test
	fun testDecodesIntRange() {
		expect(parser.parseValueOfType<IntRange>("""{"start":0,"endInclusive":1}"""))
			.toBe(IntRange(0, 1))
	}


	@Test
	fun testEncodesIntRange() {
		expect(serializer.serializeValue(IntRange(0, 1)))
			.toBe("""{"start":0,"endInclusive":1}""")
	}
}
