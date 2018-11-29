package tests

import com.github.fluidsonic.fluid.json.*
import com.winterbe.expekt.should


internal class ContextCheckingTestEncoderCodec<in Context : JSONCodingContext>(
	private val expectedContext: Context
) : JSONEncoderCodec<String, Context> {

	override fun encode(value: String, encoder: JSONEncoder<Context>) {
		encoder.context.should.equal(expectedContext)

		StringJSONCodec.encode(value, encoder)
	}


	override val encodableClass = String::class
}
