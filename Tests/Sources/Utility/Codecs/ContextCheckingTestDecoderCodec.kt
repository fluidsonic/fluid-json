package tests

import com.github.fluidsonic.fluid.json.*
import com.winterbe.expekt.should


internal class ContextCheckingTestDecoderCodec<in Context : JSONCoderContext>(
	private val expectedContext: Context
) : JSONDecoderCodec<String, Context> {

	override fun decode(valueType: JSONCodableType<in String>, decoder: JSONDecoder<out Context>): String {
		decoder.context.should.equal(expectedContext)

		return StringJSONCodec.decode(valueType, decoder)
	}


	override val decodableType = jsonCodableType<String>()
}
