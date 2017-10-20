package tests

import com.github.fluidsonic.fluid.json.ArrayJSONCodec
import com.winterbe.expekt.should
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek


internal object ArrayJSONCodecSpec : SubjectSpek<ArrayJSONCodec>({

	subject { ArrayJSONCodec }


	describe("ArrayJSONCodec") {

		it("encodes arrays") {
			subject.serialize(arrayOf("test", emptyList<Any?>()))
				.should.equal("""["test",[]]""")
		}
	}
})
