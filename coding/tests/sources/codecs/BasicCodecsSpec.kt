package tests.coding

import com.github.fluidsonic.fluid.json.*
import com.winterbe.expekt.should
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe


@Suppress("UNCHECKED_CAST")
internal object BasicCodecsSpec : Spek({

	describe("basic codecs") {
		listOf(
			Test("Any", AnyJSONTestDecoderCodec, anyData),
			Test("Array", ArrayJSONTestEncoderCodec, arrayData),
			Test("Array (non-recursive)", ArrayJSONTestEncoderCodec.NonRecursive, arrayData),
			Test("Boolean", BooleanJSONCodec, booleanData),
			Test("BooleanArray", BooleanArrayJSONCodec, booleanArrayData),
			Test("Byte", ByteJSONCodec, byteData),
			Test("ByteArray", ByteArrayJSONCodec, byteArrayData),
			Test("Char", CharJSONCodec, charData),
			Test("CharArray", CharArrayJSONCodec, charArrayData),
			Test("Double", DoubleJSONCodec, doubleData),
			Test("DoubleArray", DoubleArrayJSONCodec, doubleArrayData),
			Test("Float", FloatJSONCodec, floatData),
			Test("FloatArray", FloatArrayJSONCodec, floatArrayData),
			Test("Int", IntJSONCodec, intData),
			Test("IntArray", IntArrayJSONCodec, intArrayData),
			Test("Iterable", IterableJSONEncoderTestCodec, iterableData),
			Test("Iterable (non-recursive)", IterableJSONEncoderTestCodec.NonRecursive, iterableData),
			Test("List", ListJSONTestDecoderCodec, listData),
			Test("List (non-recursive)", ListJSONTestDecoderCodec.NonRecursive, listData),
			Test("Long", LongJSONCodec, longData),
			Test("LongArray", LongArrayJSONCodec, longArrayData),
			Test("Map", MapJSONTestCodec, mapData),
			Test("Map (non-recursive)", MapJSONTestCodec.NonRecursive, mapData),
			Test("Map (non-String keys)", MapJSONTestCodec, mapDataWithNonStringKeys),
			Test("Number", NumberJSONCodec, numberData),
			Test("Sequence", SequenceJSONTestCodec, sequenceData),
			Test("Sequence (non-recursive)", SequenceJSONTestCodec.NonRecursive, sequenceData),
			Test("Short", ShortJSONCodec, shortData),
			Test("ShortArray", ShortArrayJSONCodec, shortArrayData),
			Test("String", StringJSONCodec, stringData)
		)
			.forEach { test ->
				describe(test.name) {
					val decoderCodec = test.codec as? JSONDecoderCodec<Any, JSONCodingContext>
					if (decoderCodec != null) {
						it("decodes a value") {
							try {
								test.data.testDecoding { decoderCodec.parse(it, type = test.type as JSONCodingType<Any>) }
							}
							catch (e: Throwable) {
								throw AssertionError("${decoderCodec::class.simpleName} (for ${test.type}): ${e.message}").apply {
									stackTrace = e.stackTrace
								}
							}
						}
					}

					val encoderCodec = test.codec as? JSONEncoderCodec<Any, JSONCodingContext>
					if (encoderCodec != null) {
						it("encodes a value") {
							try {
								test.data.testEncoding { encoderCodec.serialize(it) }
							}
							catch (e: Throwable) {
								throw AssertionError("${encoderCodec::class.simpleName} (for ${test.type}): ${e.message}").apply {
									stackTrace = e.stackTrace
								}
							}
						}
					}
				}
			}


		it("Map maintains entry order") {
			MapJSONTestCodec.parse("""{ "3": 3, "1": 1, "2": 2, "0": 0 }""", jsonCodingType())
				.entries.toList()
				.should.equal(mapOf("3" to 3, "1" to 1, "2" to 2, "0" to 0).entries.toList())
		}
	}
}) {

	private class Test<Value : Any> private constructor(
		val name: String,
		val codec: JSONCodecProvider<JSONCodingContext>,
		val data: TestData<Value>,
		val type: JSONCodingType<Value>
	) {

		companion object {

			inline operator fun <reified Value : Any> invoke(
				name: String,
				codec: JSONCodecProvider<JSONCodingContext>,
				data: TestData<Value>
			) =
				Test(name = name, codec = codec, data = data, type = jsonCodingType())
		}
	}
}
