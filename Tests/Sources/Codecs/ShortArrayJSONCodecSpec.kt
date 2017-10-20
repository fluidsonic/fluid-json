package tests

import com.github.fluidsonic.fluid.json.ShortArrayJSONCodec
import com.winterbe.expekt.should
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek


internal object ShortArrayJSONCodecSpec : SubjectSpek<ShortArrayJSONCodec>({

	subject { ShortArrayJSONCodec }


	describe("ShortArrayJSONCodec") {

		it("encodes arrays") {
			subject.serialize(shortArrayOf(1, 0, -1))
				.should.equal("""[1,0,-1]""")
		}
	}
})
