package tests

import com.github.fluidsonic.fluid.json.JSONException
import com.github.fluidsonic.fluid.json.JSONSerializer
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek
import tests.JSONSerializerRejectSpec.failToSerialize


object JSONSerializerRejectSpec : SubjectSpek<JSONSerializer>({

	subject { JSONSerializer() }


	describe("JSONSerializer rejects serialization of") {

		it("non-finite float") {
			subject.failToSerialize(Float.NEGATIVE_INFINITY)
			subject.failToSerialize(Float.POSITIVE_INFINITY)
			subject.failToSerialize(Float.NaN)
		}

		it("non-finite double") {
			subject.failToSerialize(Double.NEGATIVE_INFINITY)
			subject.failToSerialize(Double.POSITIVE_INFINITY)
			subject.failToSerialize(Double.NaN)
		}

		it("non-string map keys") {
			subject.failToSerialize(mapOf(null to null))
			subject.failToSerialize(mapOf(0 to 0))
		}

		it("unsupported classes") {
			subject.failToSerialize(object {})
		}
	}
}) {

	private fun JSONSerializer.failToSerialize(value: Any?) {
		try {
			serialize(value)
			throw AssertionError("should fail with a JSONException")
		}
		catch (e: JSONException) {
			// good
		}
	}
}
