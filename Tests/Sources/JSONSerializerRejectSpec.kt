package tests

import com.github.fluidsonic.fluid.json.JSONException
import com.github.fluidsonic.fluid.json.JSONSerializer
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it


class JSONSerializerRejectSpec : Spek({

	describe("JSONSerializer rejects serialization of") {

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
}) {

	private companion object {

		fun failToSerialize(value: Any?) {
			try {
				JSONSerializer().serialize(value)
				throw AssertionError("should fail with a JSONException")
			}
			catch (e: JSONException) {
				// good
			}
		}
	}
}
