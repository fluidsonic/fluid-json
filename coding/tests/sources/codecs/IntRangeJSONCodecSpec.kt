package tests.coding

import com.github.fluidsonic.fluid.json.*
import com.winterbe.expekt.should
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe


@Suppress("UNCHECKED_CAST")
internal object IntRangeJSONCodecSpec : Spek({

	val codecs = JSONCodecProvider.of(
		IntJSONCodec,
		IntRangeJSONCodec,
		base = null
	)

	val parser = JSONCodingParser.builder()
		.decodingWith(codecs, base = null)
		.build()

	val serializer = JSONCodingSerializer.builder()
		.encodingWith(codecs, base = null)
		.build()


	describe("IntRangeJSONCodecSpec") {

		it("decodes IntRange") {
			parser.parseValueOfType<IntRange>("""{"start":0,"endInclusive":1}""")
				.should.equal(IntRange(0, 1))
		}

		it("encodes IntRange") {
			serializer.serializeValue(IntRange(0, 1))
				.should.equal("""{"start":0,"endInclusive":1}""")
		}
	}
})
