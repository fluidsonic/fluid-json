package tests.coding

import ch.tutteli.atrium.api.fluent.en_GB.*
import ch.tutteli.atrium.api.verbs.*
import io.fluidsonic.json.*
import org.junit.jupiter.api.*


@Suppress("UNCHECKED_CAST")
internal object ClosedRangeJsonCodecTest {

	val codecs = JsonCodecProvider(
		AnyJsonDecoderCodec,
		CharJsonCodec,
		ClosedRangeJsonCodec,
		DoubleJsonCodec,
		FloatJsonCodec,
		IntJsonCodec,
		LongJsonCodec,
		StringJsonCodec
	)

	val parser = JsonCodingParser.builder()
		.decodingWith(codecs, base = null)
		.build()

	val serializer = JsonCodingSerializer.builder()
		.encodingWith(codecs, base = null)
		.build()


	@Test
	fun testDecodesToCharRange() {
		expect(parser.parseValueOfType<ClosedRange<Char>>("""{"start":"\u0000","endInclusive":"a"}"""))
			.toBe(CharRange(0.toChar(), 'a'))
	}


	@Test
	fun testDecodesToWildcardComparableClosedRange() {
		expect(parser.parseValueOfType<ClosedRange<*>>("""{"start":"a","endInclusive":"b"}"""))
			.toBe("a" .. "b")
	}


	@Test
	fun testDecodesToComparableClosedRange() {
		expect(parser.parseValueOfType<ClosedRange<String>>("""{"start":"a","endInclusive":"b"}"""))
			.toBe("a" .. "b")
	}


	@Test
	fun testDecodesToDoubleClosedRange() {
		expect(parser.parseValueOfType<ClosedRange<Double>>("""{"start":1,"endInclusive":2}"""))
			.toBe(1.0 .. 2.0)
	}


	@Test
	fun testDecodesToFloatClosedRange() {
		expect(parser.parseValueOfType<ClosedRange<Float>>("""{"start":1,"endInclusive":2}"""))
			.toBe(1.0f .. 2.0f)
	}


	@Test
	fun testDecodesToIntRange() {
		expect(parser.parseValueOfType<ClosedRange<Int>>("""{"start":1,"endInclusive":2}"""))
			.toBe(IntRange(1, 2))
	}


	@Test
	fun testDecodesToLongRange() {
		expect(parser.parseValueOfType<ClosedRange<Long>>("""{"start":1,"endInclusive":2}"""))
			.toBe(LongRange(1L, 2L))
	}


	@Test
	fun testEncodesCharRange() {
		expect(serializer.serializeValue(CharRange(0.toChar(), 'a')))
			.toBe("""{"start":"\u0000","endInclusive":"a"}""")
	}


	@Test
	fun testEncodesComparableClosedRange() {
		expect(serializer.serializeValue("a" .. "b"))
			.toBe("""{"start":"a","endInclusive":"b"}""")
	}


	@Test
	fun testEncodesDoubleClosedRange() {
		expect(serializer.serializeValue(1.0 .. 2.0))
			.toBe("""{"start":1.0,"endInclusive":2.0}""")
	}


	@Test
	fun testEncodesFloatClosedRange() {
		expect(serializer.serializeValue(1.0f .. 2.0f))
			.toBe("""{"start":1.0,"endInclusive":2.0}""")
	}


	@Test
	fun testEncodesIntRange() {
		expect(serializer.serializeValue(IntRange(1, 2)))
			.toBe("""{"start":1,"endInclusive":2}""")
	}


	@Test
	fun testEncodesLongRange() {
		expect(serializer.serializeValue(LongRange(1L, 2L)))
			.toBe("""{"start":1,"endInclusive":2}""")
	}
}
