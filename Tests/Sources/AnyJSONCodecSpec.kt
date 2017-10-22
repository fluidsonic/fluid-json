package tests

import com.github.fluidsonic.fluid.json.AnyJSONCodec
import com.github.fluidsonic.fluid.json.JSONException
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek


internal object AnyJSONCodecSpec : SubjectSpek<AnyJSONCodec>({

	subject { AnyJSONCodec }


	describe("AnyJSONCodec") {

		it("fails with JSONException when encoding unsupported types") {
			try {
				subject.encode(object {}, DummyJSONEncoder())
				throw AssertionError("expected codec to fail")
			}
			catch (e: JSONException) {
				// good
			}

			try {
				subject.encode(mapOf("" to null).entries.first(), DummyJSONEncoder())
				throw AssertionError("expected codec to fail")
			}
			catch (e: JSONException) {
				// good
			}
		}
	}
})
