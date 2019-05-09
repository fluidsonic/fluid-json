package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.*
import ch.tutteli.atrium.verbs.*
import com.github.fluidsonic.fluid.json.*


internal class ContextCheckingTestCodec<in Context : JSONCodingContext>(
	private val expectedContext: Context
) : JSONCodec<String, Context> {

	override fun JSONDecoder<Context>.decode(valueType: JSONCodingType<String>): String {
		assert(context).toBe(expectedContext)

		return StringJSONCodec.run { decode(valueType) }
	}


	override fun JSONEncoder<Context>.encode(value: String) {
		assert(context).toBe(expectedContext)

		StringJSONCodec.run { encode(value) }
	}


	override val decodableType = jsonCodingType<String>()
}
