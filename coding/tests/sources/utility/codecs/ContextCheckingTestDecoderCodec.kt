package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.toBe
import ch.tutteli.atrium.verbs.assert
import com.github.fluidsonic.fluid.json.*


internal class ContextCheckingTestDecoderCodec<in Context : JSONCodingContext>(
	private val expectedContext: Context
) : JSONDecoderCodec<String, Context> {

	override fun decode(valueType: JSONCodingType<in String>, decoder: JSONDecoder<Context>): String {
		assert(decoder.context).toBe(expectedContext)

		return StringJSONCodec.decode(valueType, decoder)
	}


	override val decodableType = jsonCodingType<String>()
}
