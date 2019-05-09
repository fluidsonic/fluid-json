package tests.coding

import com.github.fluidsonic.fluid.json.*


internal object SequenceJSONTestCodec : AbstractJSONCodec<Sequence<*>, JSONCodingContext>(
	additionalProviders = listOf(AnyJSONDecoderCodec, BooleanJSONCodec, IterableJSONEncoderCodec, ListJSONDecoderCodec, NumberJSONCodec, StringJSONCodec)
) {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<Sequence<*>>) =
		SequenceJSONCodec.run { decode(valueType) }


	override fun JSONEncoder<JSONCodingContext>.encode(value: Sequence<*>) =
		SequenceJSONCodec.run { encode(value) }


	object NonRecursive : AbstractJSONCodec<Sequence<*>, JSONCodingContext>(
		additionalProviders = listOf(BooleanJSONCodec, IntJSONCodec, IterableJSONEncoderCodec, StringJSONCodec)
	) {

		override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<Sequence<*>>) =
			SequenceJSONCodec.nonRecursive.run { decode(valueType) }


		override fun JSONEncoder<JSONCodingContext>.encode(value: Sequence<*>) =
			SequenceJSONCodec.nonRecursive.run { encode(value) }
	}
}
