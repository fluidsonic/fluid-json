package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.*
import ch.tutteli.atrium.verbs.*
import com.github.fluidsonic.fluid.json.*
import org.junit.jupiter.api.*


@Suppress("UNCHECKED_CAST")
internal object ClosedRangeJSONCodecTest {

	val codecs = JSONCodecProvider(
		AnyJSONDecoderCodec,
		CharJSONCodec,
		ClosedRangeJSONCodec,
		DoubleJSONCodec,
		FloatJSONCodec,
		IntJSONCodec,
		LongJSONCodec,
		StringJSONCodec
	)

	val parser = JSONCodingParser.builder()
		.decodingWith(codecs, base = null)
		.build()

	val serializer = JSONCodingSerializer.builder()
		.encodingWith(codecs, base = null)
		.build()


	@Test
	fun testDecodesToCharRange() {
		assert(parser.parseValueOfType<ClosedRange<Char>>("""{"start":"\u0000","endInclusive":"a"}"""))
			.toBe(CharRange(0.toChar(), 'a'))
	}


	@Test
	fun testDecodesToWildcardComparableClosedRange() {
		assert(parser.parseValueOfType<ClosedRange<*>>("""{"start":"a","endInclusive":"b"}"""))
			.toBe("a" .. "b")
	}


	@Test
	fun testDecodesToComparableClosedRange() {
		assert(parser.parseValueOfType<ClosedRange<String>>("""{"start":"a","endInclusive":"b"}"""))
			.toBe("a" .. "b")
	}


	@Test
	fun testDecodesToDoubleClosedRange() {
		assert(parser.parseValueOfType<ClosedRange<Double>>("""{"start":1,"endInclusive":2}"""))
			.toBe(1.0 .. 2.0)
	}


	@Test
	fun testDecodesToFloatClosedRange() {
		assert(parser.parseValueOfType<ClosedRange<Float>>("""{"start":1,"endInclusive":2}"""))
			.toBe(1.0f .. 2.0f)
	}


	@Test
	fun testDecodesToIntRange() {
		assert(parser.parseValueOfType<ClosedRange<Int>>("""{"start":1,"endInclusive":2}"""))
			.toBe(IntRange(1, 2))
	}


	@Test
	fun testDecodesToLongRange() {
		assert(parser.parseValueOfType<ClosedRange<Long>>("""{"start":1,"endInclusive":2}"""))
			.toBe(LongRange(1L, 2L))
	}


	@Test
	fun testEncodesCharRange() {
		assert(serializer.serializeValue(CharRange(0.toChar(), 'a')))
			.toBe("""{"start":"\u0000","endInclusive":"a"}""")
	}


	@Test
	fun testencodesComparableClosedRange() {
		assert(serializer.serializeValue("a" .. "b"))
			.toBe("""{"start":"a","endInclusive":"b"}""")
	}


	@Test
	fun testEncodesDoubleClosedRange() {
		assert(serializer.serializeValue(1.0 .. 2.0))
			.toBe("""{"start":1.0,"endInclusive":2.0}""")
	}


	@Test
	fun testEncodesFloatClosedRange() {
		assert(serializer.serializeValue(1.0f .. 2.0f))
			.toBe("""{"start":1.0,"endInclusive":2.0}""")
	}


	@Test
	fun testEncodesIntRange() {
		assert(serializer.serializeValue(IntRange(1, 2)))
			.toBe("""{"start":1,"endInclusive":2}""")
	}


	@Test
	fun testEncodesLongRange() {
		assert(serializer.serializeValue(LongRange(1L, 2L)))
			.toBe("""{"start":1,"endInclusive":2}""")
	}
}
