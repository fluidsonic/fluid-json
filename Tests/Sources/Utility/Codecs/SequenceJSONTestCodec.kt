package tests

import com.github.fluidsonic.fluid.json.*


internal object SequenceJSONTestCodec : AbstractJSONCodec<Sequence<*>, JSONCoderContext>(
	additionalProviders = listOf(AnyJSONDecoderCodec, BooleanJSONCodec, IterableJSONEncoderCodec, ListJSONDecoderCodec, NumberJSONCodec, StringJSONCodec)
) {

	override fun decode(valueType: JSONCodableType<in Sequence<*>>, decoder: JSONDecoder<JSONCoderContext>) =
		SequenceJSONCodec.decode(valueType, decoder)


	override fun encode(value: Sequence<*>, encoder: JSONEncoder<JSONCoderContext>) =
		SequenceJSONCodec.encode(value, encoder)


	object NonRecursive : AbstractJSONCodec<Sequence<*>, JSONCoderContext>(
		additionalProviders = listOf(BooleanJSONCodec, IntJSONCodec, IterableJSONEncoderCodec, StringJSONCodec)
	) {

		override fun decode(valueType: JSONCodableType<in Sequence<*>>, decoder: JSONDecoder<JSONCoderContext>) =
			SequenceJSONCodec.nonRecursive.decode(valueType, decoder)


		override fun encode(value: Sequence<*>, encoder: JSONEncoder<JSONCoderContext>) =
			SequenceJSONCodec.nonRecursive.encode(value, encoder)
	}
}
