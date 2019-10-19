package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.*
import ch.tutteli.atrium.verbs.*
import io.fluidsonic.json.*
import org.junit.jupiter.api.*


@Suppress("UNCHECKED_CAST")
internal object LongRangeJsonCodecTest {

	val codecs = JsonCodecProvider(
		LongJsonCodec,
		LongRangeJsonCodec
	)

	val parser = JsonCodingParser.builder()
		.decodingWith(codecs, base = null)
		.build()

	val serializer = JsonCodingSerializer.builder()
		.encodingWith(codecs, base = null)
		.build()


	@Test
	fun testDecodesLongRange() {
		assert(parser.parseValueOfType<LongRange>("""{"start":0,"endInclusive":1}"""))
			.toBe(LongRange(0L, 1L))
	}


	@Test
	fun encodesLongRange() {
		assert(serializer.serializeValue(LongRange(0L, 1L)))
			.toBe("""{"start":0,"endInclusive":1}""")
	}
}
