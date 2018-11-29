package tests.coding

import com.github.fluidsonic.fluid.json.*
import com.winterbe.expekt.should


internal class ContextCheckingTestDecoderCodec<in Context : JSONCodingContext>(
	private val expectedContext: Context
) : JSONDecoderCodec<String, Context> {

	override fun decode(valueType: JSONCodingType<in String>, decoder: JSONDecoder<Context>): String {
		decoder.context.should.equal(expectedContext)

		return StringJSONCodec.decode(valueType, decoder)
	}


	override val decodableType = jsonCodingType<String>()
}
