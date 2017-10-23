package tests

import com.github.fluidsonic.fluid.json.JSONCoderContext
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.JSONEncoderCodec
import com.github.fluidsonic.fluid.json.StringJSONCodec
import com.winterbe.expekt.should


internal class ContextCheckingTestEncoderCodec<in Context : JSONCoderContext>(
	private val expectedContext: Context
) : JSONEncoderCodec<String, Context> {

	override fun encode(value: String, encoder: JSONEncoder<out Context>) {
		encoder.context.should.equal(expectedContext)

		StringJSONCodec.encode(value, encoder)
	}


	override val encodableClasses = setOf(String::class.java)
}
