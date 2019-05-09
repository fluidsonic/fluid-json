package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.*
import ch.tutteli.atrium.verbs.*
import com.github.fluidsonic.fluid.json.*


internal class ContextCheckingTestDecoderCodec<in Context : JSONCodingContext>(
	private val expectedContext: Context
) : JSONDecoderCodec<String, Context> {

	override fun JSONDecoder<Context>.decode(valueType: JSONCodingType<String>): String {
		assert(context).toBe(expectedContext)

		return StringJSONCodec.run { decode(valueType) }
	}


	override val decodableType = jsonCodingType<String>()
}
