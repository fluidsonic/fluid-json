package tests.coding

import com.github.fluidsonic.fluid.json.*
import com.winterbe.expekt.should


internal class ContextCheckingTestCodec<in Context : JSONCodingContext>(
	private val expectedContext: Context
) : JSONCodec<String, Context> {

	override fun decode(valueType: JSONCodingType<in String>, decoder: JSONDecoder<Context>): String {
		decoder.context.should.equal(expectedContext)

		return StringJSONCodec.decode(valueType, decoder)
	}


	override fun encode(value: String, encoder: JSONEncoder<Context>) {
		encoder.context.should.equal(expectedContext)

		StringJSONCodec.encode(value, encoder)
	}


	override val decodableType = jsonCodingType<String>()
}
