package tests.coding

import ch.tutteli.atrium.api.fluent.en_GB.*
import ch.tutteli.atrium.api.verbs.*
import io.fluidsonic.json.*


internal class ContextCheckingTestDecoderCodec<in Context : JsonCodingContext>(
	private val expectedContext: Context
) : JsonDecoderCodec<String, Context> {

	override fun JsonDecoder<Context>.decode(valueType: JsonCodingType<String>): String {
		expect(context).toBe(expectedContext)

		return StringJsonCodec.run { decode(valueType) }
	}


	override val decodableType = jsonCodingType<String>()
}
