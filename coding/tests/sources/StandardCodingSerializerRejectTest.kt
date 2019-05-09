package tests.coding

import com.github.fluidsonic.fluid.json.*
import org.junit.jupiter.api.*


internal object StandardCodingSerializerRejectTest {

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
		val serializer = StandardCodingSerializer(JSONCodingContext.empty) { destination, context ->
			JSONEncoder.builder(context)
				.codecs()
				.destination(destination)
				.build()
		}

		try {
			serializer.serializeValue(value)
			throw AssertionError("should fail with a JSONException")
		}
		catch (e: JSONException) {
			// good
		}
	}
}

