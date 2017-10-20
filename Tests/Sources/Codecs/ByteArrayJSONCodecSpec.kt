package tests

import com.github.fluidsonic.fluid.json.ByteArrayJSONCodec
import com.winterbe.expekt.should
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek


internal object ByteArrayJSONCodecSpec : SubjectSpek<ByteArrayJSONCodec>({

	subject { ByteArrayJSONCodec }


	describe("ByteArrayJSONCodec") {

		it("encodes arrays") {
			subject.serialize(byteArrayOf(1, 0, -1))
				.should.equal("""[1,0,-1]""")
		}
	}
})
