package tests

import com.github.fluidsonic.fluid.json.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.style.specification.describe


internal object StandardSerializerRejectSpec : Spek({

	describe("StandardSerializer rejects serialization of") {

		it("non-finite float") {
			failToSerialize(Float.NEGATIVE_INFINITY)
			failToSerialize(Float.POSITIVE_INFINITY)
			failToSerialize(Float.NaN)
		}

		it("non-finite double") {
			failToSerialize(Double.NEGATIVE_INFINITY)
			failToSerialize(Double.POSITIVE_INFINITY)
			failToSerialize(Double.NaN)
		}

		it("non-string map keys") {
			failToSerialize(mapOf(null to null))
			failToSerialize(mapOf(0 to 0))
		}

		it("unsupported classes") {
			failToSerialize(object {})
		}
	}
})


// TODO move the following method inside the object above once KT-19796 is fixed
// https://youtrack.jetbrains.com/issue/KT-19796

@Suppress("unused")
private fun TestBody.failToSerialize(value: Any?) {
	val serializer = StandardSerializer(JSONCoderContext.empty) { destination, context ->
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
