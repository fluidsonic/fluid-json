package tests.coding

import com.github.fluidsonic.fluid.json.*
import com.winterbe.expekt.should
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe


@Suppress("UNCHECKED_CAST")
internal object CharRangeJSONCodecSpec : Spek({

	val codecs = JSONCodecProvider.of(
		CharJSONCodec,
		CharRangeJSONCodec,
		base = null
	)

	val parser = JSONCodingParser.builder()
		.decodingWith(codecs, base = null)
		.build()

	val serializer = JSONCodingSerializer.builder()
		.encodingWith(codecs, base = null)
		.build()


	describe("CharRangeJSONCodecSpec") {

		it("decodes CharRange") {
			parser.parseValueOfType<CharRange>("""{"start":"\u0000","endInclusive":"a"}""")
				.should.equal(CharRange(0.toChar(), 'a'))
		}

		it("encodes CharRange") {
			serializer.serializeValue(CharRange(0.toChar(), 'a'))
				.should.equal("""{"start":"\u0000","endInclusive":"a"}""")
		}
	}
})
