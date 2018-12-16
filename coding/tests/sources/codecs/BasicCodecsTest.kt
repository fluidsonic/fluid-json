package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.toBe
import ch.tutteli.atrium.verbs.assert
import com.github.fluidsonic.fluid.json.*
import org.junit.jupiter.api.DynamicContainer
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory


@Suppress("UNCHECKED_CAST")
internal object BasicCodecsTest {

	private val cases = listOf(
		TestCase("Any", AnyJSONTestDecoderCodec, anyData),
		TestCase("Array", ArrayJSONTestEncoderCodec, arrayData),
		TestCase("Array (non-recursive)", ArrayJSONTestEncoderCodec.NonRecursive, arrayData),
		TestCase("Boolean", BooleanJSONCodec, booleanData),
		TestCase("BooleanArray", BooleanArrayJSONCodec, booleanArrayData),
		TestCase("Byte", ByteJSONCodec, byteData),
		TestCase("ByteArray", ByteArrayJSONCodec, byteArrayData),
		TestCase("Char", CharJSONCodec, charData),
		TestCase("CharArray", CharArrayJSONCodec, charArrayData),
		TestCase("Double", DoubleJSONCodec, doubleData),
		TestCase("DoubleArray", DoubleArrayJSONCodec, doubleArrayData),
		TestCase("Float", FloatJSONCodec, floatData),
		TestCase("FloatArray", FloatArrayJSONCodec, floatArrayData),
		TestCase("Int", IntJSONCodec, intData),
		TestCase("IntArray", IntArrayJSONCodec, intArrayData),
		TestCase("Iterable", IterableJSONEncoderTestCodec, iterableData),
		TestCase("Iterable (non-recursive)", IterableJSONEncoderTestCodec.NonRecursive, iterableData),
		TestCase("List", ListJSONTestDecoderCodec, listData),
		TestCase("List (non-recursive)", ListJSONTestDecoderCodec.NonRecursive, listData),
		TestCase("Long", LongJSONCodec, longData),
		TestCase("LongArray", LongArrayJSONCodec, longArrayData),
		TestCase("Map", MapJSONTestCodec, mapData),
		TestCase("Map (non-recursive)", MapJSONTestCodec.NonRecursive, mapData),
		TestCase("Map (non-String keys)", MapJSONTestCodec, mapDataWithNonStringKeys),
		TestCase("Number", NumberJSONCodec, numberData),
		TestCase("Sequence", SequenceJSONTestCodec, sequenceData),
		TestCase("Sequence (non-recursive)", SequenceJSONTestCodec.NonRecursive, sequenceData),
		TestCase("Short", ShortJSONCodec, shortData),
		TestCase("ShortArray", ShortArrayJSONCodec, shortArrayData),
		TestCase("String", StringJSONCodec, stringData)
	)


	private fun buildTests(case: TestCase<*>) = listOfNotNull(
		(case.codec as? JSONDecoderCodec<Any, JSONCodingContext>)?.let { decoderCodec ->
			DynamicTest.dynamicTest("is decodable") {
				try {
					case.data.testDecoding { decoderCodec.parse(it, type = case.type as JSONCodingType<Any>) }
				}
				catch (e: Throwable) {
					throw AssertionError("${decoderCodec::class.simpleName} (for ${case.type}): ${e.message}").apply {
						stackTrace = e.stackTrace
					}
				}
			}
		},

		(case.codec as? JSONEncoderCodec<Any, JSONCodingContext>)?.let { encoderCodec ->
			DynamicTest.dynamicTest("is encodable") {
				try {
					case.data.testEncoding { encoderCodec.serialize(it) }
				}
				catch (e: Throwable) {
					throw AssertionError("${encoderCodec::class.simpleName} (for ${case.type}): ${e.message}").apply {
						stackTrace = e.stackTrace
					}
				}
			}
		}
	)


	@TestFactory
	fun test() = cases
		.map { case ->
			DynamicContainer.dynamicContainer(
				case.name,
				buildTests(case = case)
			)
		}


	@Test
	fun testMaintainsMapEntryOrder() {
		assert(
			MapJSONTestCodec.parse("""{ "3": 3, "1": 1, "2": 2, "0": 0 }""", jsonCodingType())
				.entries.toList()
		)
			.toBe(mapOf("3" to 3, "1" to 1, "2" to 2, "0" to 0).entries.toList())
	}


	private class TestCase<Value : Any> private constructor(
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
				TestCase(name = name, codec = codec, data = data, type = jsonCodingType())
		}
	}
}
