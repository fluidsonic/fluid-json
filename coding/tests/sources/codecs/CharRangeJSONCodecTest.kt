package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.toBe
import ch.tutteli.atrium.verbs.assert
import com.github.fluidsonic.fluid.json.*
import org.junit.jupiter.api.Test


@Suppress("UNCHECKED_CAST")
internal object CharRangeJSONCodecTest {

	val codecs = JSONCodecProvider(
		CharJSONCodec,
		CharRangeJSONCodec
	)

	val parser = JSONCodingParser.builder()
		.decodingWith(codecs, base = null)
		.build()

	val serializer = JSONCodingSerializer.builder()
		.encodingWith(codecs, base = null)
		.build()


	@Test
	fun testDecodesCharRange() {
		assert(parser.parseValueOfType<CharRange>("""{"start":"\u0000","endInclusive":"a"}"""))
			.toBe(CharRange(0.toChar(), 'a'))
	}


	@Test
	fun testEncodesCharRange() {
		assert(serializer.serializeValue(CharRange(0.toChar(), 'a')))
			.toBe("""{"start":"\u0000","endInclusive":"a"}""")
	}
}
