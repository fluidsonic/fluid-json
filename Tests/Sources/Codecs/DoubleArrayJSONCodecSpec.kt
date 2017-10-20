package tests

import com.github.fluidsonic.fluid.json.DoubleArrayJSONCodec
import com.winterbe.expekt.should
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek


internal object DoubleArrayJSONCodecSpec : SubjectSpek<DoubleArrayJSONCodec>({

	subject { DoubleArrayJSONCodec }


	describe("DoubleArrayJSONCodec") {

		it("encodes arrays") {
			subject.serialize(doubleArrayOf(1.0, 0.0, -1.0))
				.should.equal("""[1.0,0.0,-1.0]""")
		}
	}
})
