package tests.coding

import com.github.fluidsonic.fluid.json.*
import com.winterbe.expekt.should
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe


@Suppress("UNCHECKED_CAST")
internal object ClosedRangeJSONCodecSpec : Spek({

	val codecs = JSONCodecProvider.of(
		AnyJSONDecoderCodec,
		CharJSONCodec,
		ClosedRangeJSONCodec,
		DoubleJSONCodec,
		FloatJSONCodec,
		IntJSONCodec,
		LongJSONCodec,
		StringJSONCodec,
		base = null
	)

	val parser = JSONCodingParser.builder()
		.decodingWith(codecs, base = null)
		.build()

	val serializer = JSONCodingSerializer.builder()
		.encodingWith(codecs, base = null)
		.build()


	describe("ClosedRangeJSONCodecSpec") {

		it("decodes to CharRange") {
			parser.parseValueOfType<ClosedRange<Char>>("""{"start":"\u0000","endInclusive":"a"}""")
				.should.equal(CharRange(0.toChar(), 'a'))
		}

		it("decodes to any Comparable ClosedRange") {
			parser.parseValueOfType<ClosedRange<*>>("""{"start":"a","endInclusive":"b"}""")
				.should.equal("a" .. "b")
		}

		it("decodes to Comparable ClosedRange") {
			parser.parseValueOfType<ClosedRange<String>>("""{"start":"a","endInclusive":"b"}""")
				.should.equal("a" .. "b")
		}

		it("decodes to Double ClosedRange") {
			parser.parseValueOfType<ClosedRange<Double>>("""{"start":1,"endInclusive":2}""")
				.should.equal(1.0 .. 2.0)
		}

		it("decodes to Float ClosedRange") {
			parser.parseValueOfType<ClosedRange<Float>>("""{"start":1,"endInclusive":2}""")
				.should.equal(1.0f .. 2.0f)
		}

		it("decodes to IntRange") {
			parser.parseValueOfType<ClosedRange<Int>>("""{"start":1,"endInclusive":2}""")
				.should.equal(IntRange(1, 2))
		}

		it("decodes to LongRange") {
			parser.parseValueOfType<ClosedRange<Long>>("""{"start":1,"endInclusive":2}""")
				.should.equal(LongRange(1L, 2L))
		}

		it("encodes CharRange") {
			serializer.serializeValue(CharRange(0.toChar(), 'a'))
				.should.equal("""{"start":"\u0000","endInclusive":"a"}""")
		}

		it("encodes Comparable ClosedRange") {
			serializer.serializeValue("a" .. "b")
				.should.equal("""{"start":"a","endInclusive":"b"}""")
		}

		it("encodes Double ClosedRange") {
			serializer.serializeValue(1.0 .. 2.0)
				.should.equal("""{"start":1.0,"endInclusive":2.0}""")
		}

		it("encodes Float ClosedRange") {
			serializer.serializeValue(1.0f .. 2.0f)
				.should.equal("""{"start":1.0,"endInclusive":2.0}""")
		}

		it("encodes IntRange") {
			serializer.serializeValue(IntRange(1, 2))
				.should.equal("""{"start":1,"endInclusive":2}""")
		}

		it("encodes LongRange") {
			serializer.serializeValue(LongRange(1L, 2L))
				.should.equal("""{"start":1,"endInclusive":2}""")
		}
	}
})
