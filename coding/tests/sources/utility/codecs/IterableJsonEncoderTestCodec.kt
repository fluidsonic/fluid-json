package tests.coding

import io.fluidsonic.json.*


internal object IterableJsonEncoderTestCodec : AbstractJsonEncoderCodec<Iterable<*>, JsonCodingContext>(
	additionalProviders = listOf(AnyJsonDecoderCodec, BooleanJsonCodec, NumberJsonCodec, StringJsonCodec)
) {

	override fun JsonEncoder<JsonCodingContext>.encode(value: Iterable<*>) =
		IterableJsonEncoderCodec.run { encode(value) }


	object NonRecursive : AbstractJsonEncoderCodec<Iterable<*>, JsonCodingContext>(
		additionalProviders = listOf(BooleanJsonCodec, IntJsonCodec, StringJsonCodec)
	) {

		override fun JsonEncoder<JsonCodingContext>.encode(value: Iterable<*>) =
			IterableJsonEncoderCodec.nonRecursive.run { encode(value) }
	}
}
