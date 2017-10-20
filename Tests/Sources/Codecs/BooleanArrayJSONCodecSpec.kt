package tests

import com.github.fluidsonic.fluid.json.BooleanArrayJSONCodec
import com.winterbe.expekt.should
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek


internal object BooleanArrayJSONCodecSpec : SubjectSpek<BooleanArrayJSONCodec>({

	subject { BooleanArrayJSONCodec }


	describe("BooleanArrayJSONCodec") {

		it("encodes arrays") {
			subject.serialize(booleanArrayOf(true, false, true))
				.should.equal("""[true,false,true]""")
		}
	}
})
