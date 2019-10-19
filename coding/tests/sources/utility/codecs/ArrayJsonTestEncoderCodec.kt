package tests.coding

import io.fluidsonic.json.*


internal object ArrayJsonTestEncoderCodec : AbstractJsonEncoderCodec<Array<*>, JsonCodingContext>(
	additionalProviders = listOf(StringJsonCodec)
) {

	override fun JsonEncoder<JsonCodingContext>.encode(value: Array<*>) =
		ArrayJsonCodec.run { encode(value) }


	object NonRecursive : AbstractJsonEncoderCodec<Array<*>, JsonCodingContext>(
		additionalProviders = listOf(StringJsonCodec)
	) {

		override fun JsonEncoder<JsonCodingContext>.encode(value: Array<*>) =
			ArrayJsonCodec.nonRecursive.run { encode(value) }
	}
}
