package tests

import com.github.fluidsonic.fluid.json.FloatArrayJSONCodec
import com.winterbe.expekt.should
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek


internal object FloatArrayJSONCodecSpec : SubjectSpek<FloatArrayJSONCodec>({

	subject { FloatArrayJSONCodec }


	describe("FloatArrayJSONCodec") {

		it("encodes arrays") {
			subject.serialize(floatArrayOf(1.0f, 0.0f, -1.0f))
				.should.equal("""[1.0,0.0,-1.0]""")
		}
	}
})
