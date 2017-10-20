package tests

import com.github.fluidsonic.fluid.json.LongArrayJSONCodec
import com.winterbe.expekt.should
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek


internal object LongArrayJSONCodecSpec : SubjectSpek<LongArrayJSONCodec>({

	subject { LongArrayJSONCodec }


	describe("LongArrayJSONCodec") {

		it("encodes arrays") {
			subject.serialize(longArrayOf(1, 0, -1))
				.should.equal("""[1,0,-1]""")
		}
	}
})
