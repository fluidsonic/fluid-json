package tests

import com.github.fluidsonic.fluid.json.IntArrayJSONCodec
import com.winterbe.expekt.should
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek


internal object IntArrayJSONCodecSpec : SubjectSpek<IntArrayJSONCodec>({

	subject { IntArrayJSONCodec }


	describe("IntArrayJSONCodec") {

		it("encodes arrays") {
			subject.serialize(intArrayOf(1, 0, -1))
				.should.equal("""[1,0,-1]""")
		}
	}
})
