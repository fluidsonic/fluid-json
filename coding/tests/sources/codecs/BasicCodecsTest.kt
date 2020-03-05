package tests.coding

import ch.tutteli.atrium.api.fluent.en_GB.*
import ch.tutteli.atrium.api.verbs.*
import io.fluidsonic.json.*
import org.junit.jupiter.api.*


@Suppress("UNCHECKED_CAST")
internal object BasicCodecsTest {

	private val cases = listOf(
		TestCase("Any", AnyJsonTestDecoderCodec, anyData),
		TestCase("Array", ArrayJsonTestEncoderCodec, arrayData),
		TestCase("Array (non-recursive)", ArrayJsonTestEncoderCodec.NonRecursive, arrayData),
		TestCase("Boolean", BooleanJsonCodec, booleanData),
		TestCase("BooleanArray", BooleanArrayJsonCodec, booleanArrayData),
		TestCase("Byte", ByteJsonCodec, byteData),
		TestCase("ByteArray", ByteArrayJsonCodec, byteArrayData),
		TestCase("Char", CharJsonCodec, charData),
		TestCase("CharArray", CharArrayJsonCodec, charArrayData),
		TestCase("Collection", CollectionJsonTestCodec, collectionData),
		TestCase("Collection (non-recursive)", CollectionJsonTestCodec.NonRecursive, collectionData),
		TestCase("Double", DoubleJsonCodec, doubleData),
		TestCase("DoubleArray", DoubleArrayJsonCodec, doubleArrayData),
		TestCase("Float", FloatJsonCodec, floatData),
		TestCase("FloatArray", FloatArrayJsonCodec, floatArrayData),
		TestCase("Int", IntJsonCodec, intData),
		TestCase("IntArray", IntArrayJsonCodec, intArrayData),
		TestCase("Iterable", IterableJsonEncoderTestCodec, iterableData),
		TestCase("Iterable (non-recursive)", IterableJsonEncoderTestCodec.NonRecursive, iterableData),
		TestCase("List", ListJsonTestDecoderCodec, listData),
		TestCase("List (non-recursive)", ListJsonTestDecoderCodec.NonRecursive, listData),
		TestCase("Long", LongJsonCodec, longData),
		TestCase("LongArray", LongArrayJsonCodec, longArrayData),
		TestCase("Map", MapJsonTestCodec, mapData),
		TestCase("Map (non-recursive)", MapJsonTestCodec.NonRecursive, mapData),
		TestCase("Map (non-String keys)", MapJsonTestCodec, mapDataWithNonStringKeys),
		TestCase("Number", NumberJsonCodec, numberData),
		TestCase("Sequence", SequenceJsonTestCodec, sequenceData),
		TestCase("Sequence (non-recursive)", SequenceJsonTestCodec.NonRecursive, sequenceData),
		TestCase("Set", SetJsonTestDecoderCodec, setData),
		TestCase("Set (non-recursive)", SetJsonTestDecoderCodec.NonRecursive, setData),
		TestCase("Short", ShortJsonCodec, shortData),
		TestCase("ShortArray", ShortArrayJsonCodec, shortArrayData),
		TestCase("String", StringJsonCodec, stringData)
	)


	private fun buildTests(case: TestCase<*>) = listOfNotNull(
		(case.codec as? JsonDecoderCodec<Any, JsonCodingContext>)?.let { decoderCodec ->
			DynamicTest.dynamicTest("is decodable") {
				try {
					case.data.testDecoding { decoderCodec.parse(it, type = case.type as JsonCodingType<Any>) }
				}
				catch (e: Throwable) {
					throw AssertionError("${decoderCodec::class.simpleName} (for ${case.type}): ${e.message}").apply {
						stackTrace = e.stackTrace
					}
				}
			}
		},

		(case.codec as? JsonEncoderCodec<Any, JsonCodingContext>)?.let { encoderCodec ->
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
		expect(
			MapJsonTestCodec.parse("""{ "3": 3, "1": 1, "2": 2, "0": 0 }""", jsonCodingType())
				.entries.toList()
		)
			.toBe(mapOf("3" to 3, "1" to 1, "2" to 2, "0" to 0).entries.toList())
	}


	private class TestCase<Value : Any> private constructor(
		val name: String,
		val codec: JsonCodecProvider<JsonCodingContext>,
		val data: TestData<Value>,
		val type: JsonCodingType<Value>
	) {

		companion object {

			inline operator fun <reified Value : Any> invoke(
				name: String,
				codec: JsonCodecProvider<JsonCodingContext>,
				data: TestData<Value>
			) =
				TestCase(name = name, codec = codec, data = data, type = jsonCodingType())
		}
	}
}
