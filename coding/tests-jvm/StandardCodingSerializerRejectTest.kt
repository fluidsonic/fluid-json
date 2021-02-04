package tests.coding

import io.fluidsonic.json.*
import kotlin.test.*


class StandardCodingSerializerRejectTest {

	@Test
	fun testNonFiniteFloat() {
		failToSerialize(Float.NEGATIVE_INFINITY)
		failToSerialize(Float.POSITIVE_INFINITY)
		failToSerialize(Float.NaN)
	}


	@Test
	fun testNonFiniteDouble() {
		failToSerialize(Double.NEGATIVE_INFINITY)
		failToSerialize(Double.POSITIVE_INFINITY)
		failToSerialize(Double.NaN)
	}


	@Test
	fun testNonStringMapKeys() {
		failToSerialize(mapOf(null to null))
		failToSerialize(mapOf(0 to 0))
	}


	@Test
	fun testUnsupportedClasses() {
		failToSerialize(object {})
	}


	private fun failToSerialize(value: Any?) {
		val serializer = StandardCodingSerializer(JsonCodingContext.empty) { destination, context ->
			JsonEncoder.builder(context)
				.codecs()
				.destination(destination)
				.build()
		}

		try {
			serializer.serializeValue(value)
			throw AssertionError("should fail with a JsonException")
		}
		catch (e: JsonException) {
			// good
		}
	}
}

