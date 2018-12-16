package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.toBe
import ch.tutteli.atrium.verbs.assert
import com.github.fluidsonic.fluid.json.*


internal class ContextCheckingTestCodec<in Context : JSONCodingContext>(
	private val expectedContext: Context
) : JSONCodec<String, Context> {

	override fun decode(valueType: JSONCodingType<in String>, decoder: JSONDecoder<Context>): String {
		assert(decoder.context).toBe(expectedContext)

		return StringJSONCodec.decode(valueType, decoder)
	}


	override fun encode(value: String, encoder: JSONEncoder<Context>) {
		assert(encoder.context).toBe(expectedContext)

		StringJSONCodec.encode(value, encoder)
	}


	override val decodableType = jsonCodingType<String>()
}
