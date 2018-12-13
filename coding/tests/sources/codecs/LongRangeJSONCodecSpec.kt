package tests.coding

import com.github.fluidsonic.fluid.json.*
import com.winterbe.expekt.should
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe


@Suppress("UNCHECKED_CAST")
internal object LongRangeJSONCodecSpec : Spek({

	val codecs = JSONCodecProvider.of(
		LongJSONCodec,
		LongRangeJSONCodec,
		base = null
	)

	val parser = JSONCodingParser.builder()
		.decodingWith(codecs, base = null)
		.build()

	val serializer = JSONCodingSerializer.builder()
		.encodingWith(codecs, base = null)
		.build()


	describe("LongRangeJSONCodecSpec") {

		it("decodes LongRange") {
			parser.parseValueOfType<LongRange>("""{"start":0,"endInclusive":1}""")
				.should.equal(LongRange(0L, 1L))
		}

		it("encodes LongRange") {
			serializer.serializeValue(LongRange(0L, 1L))
				.should.equal("""{"start":0,"endInclusive":1}""")
		}
	}
})
