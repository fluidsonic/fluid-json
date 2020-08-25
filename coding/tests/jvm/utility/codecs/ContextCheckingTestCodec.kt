package tests.coding

import io.fluidsonic.json.*


internal class ContextCheckingTestCodec<in Context : JsonCodingContext>(
	private val expectedContext: Context
) : JsonCodec<String, Context> {

	override fun JsonDecoder<Context>.decode(valueType: JsonCodingType<String>): String {
		expect(context).toBe(expectedContext)

		return StringJsonCodec.run { decode(valueType) }
	}


	override fun JsonEncoder<Context>.encode(value: String) {
		expect(context).toBe(expectedContext)

		StringJsonCodec.run { encode(value) }
	}


	override val decodableType = jsonCodingType<String>()
}
