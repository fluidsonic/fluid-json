package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.*
import ch.tutteli.atrium.verbs.*
import io.fluidsonic.json.*


internal class ContextCheckingTestCodec<in Context : JsonCodingContext>(
	private val expectedContext: Context
) : JsonCodec<String, Context> {

	override fun JsonDecoder<Context>.decode(valueType: JsonCodingType<String>): String {
		assert(context).toBe(expectedContext)

		return StringJsonCodec.run { decode(valueType) }
	}


	override fun JsonEncoder<Context>.encode(value: String) {
		assert(context).toBe(expectedContext)

		StringJsonCodec.run { encode(value) }
	}


	override val decodableType = jsonCodingType<String>()
}
