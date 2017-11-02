package tests

import com.github.fluidsonic.fluid.json.*


internal object ListJSONTestDecoderCodec : AbstractJSONDecoderCodec<List<*>, JSONCoderContext>(
	additionalProviders = listOf(AnyJSONDecoderCodec, BooleanJSONCodec, NumberJSONCodec, StringJSONCodec)
) {

	override fun decode(valueType: JSONCodableType<in List<*>>, decoder: JSONDecoder<JSONCoderContext>) =
		ListJSONDecoderCodec.decode(valueType, decoder)


	object NonRecursive : AbstractJSONDecoderCodec<List<*>, JSONCoderContext>(
		additionalProviders = listOf(BooleanJSONCodec, IntJSONCodec, StringJSONCodec)
	) {

		override fun decode(valueType: JSONCodableType<in List<*>>, decoder: JSONDecoder<JSONCoderContext>) =
			ListJSONDecoderCodec.nonRecursive.decode(valueType, decoder)
	}
}
