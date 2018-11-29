package tests.coding

import com.github.fluidsonic.fluid.json.*


internal object SequenceJSONTestCodec : AbstractJSONCodec<Sequence<*>, JSONCodingContext>(
	additionalProviders = listOf(AnyJSONDecoderCodec, BooleanJSONCodec, IterableJSONEncoderCodec, ListJSONDecoderCodec, NumberJSONCodec, StringJSONCodec)
) {

	override fun decode(valueType: JSONCodingType<in Sequence<*>>, decoder: JSONDecoder<JSONCodingContext>) =
		SequenceJSONCodec.decode(valueType, decoder)


	override fun encode(value: Sequence<*>, encoder: JSONEncoder<JSONCodingContext>) =
		SequenceJSONCodec.encode(value, encoder)


	object NonRecursive : AbstractJSONCodec<Sequence<*>, JSONCodingContext>(
		additionalProviders = listOf(BooleanJSONCodec, IntJSONCodec, IterableJSONEncoderCodec, StringJSONCodec)
	) {

		override fun decode(valueType: JSONCodingType<in Sequence<*>>, decoder: JSONDecoder<JSONCodingContext>) =
			SequenceJSONCodec.nonRecursive.decode(valueType, decoder)


		override fun encode(value: Sequence<*>, encoder: JSONEncoder<JSONCodingContext>) =
			SequenceJSONCodec.nonRecursive.encode(value, encoder)
	}
}
