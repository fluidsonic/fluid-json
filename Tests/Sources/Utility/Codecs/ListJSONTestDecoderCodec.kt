package tests

import com.github.fluidsonic.fluid.json.*


internal object ListJSONTestDecoderCodec : AbstractJSONDecoderCodec<List<*>, JSONCodingContext>(
	additionalProviders = listOf(AnyJSONDecoderCodec, BooleanJSONCodec, NumberJSONCodec, StringJSONCodec)
) {

	override fun decode(valueType: JSONCodingType<in List<*>>, decoder: JSONDecoder<JSONCodingContext>) =
		ListJSONDecoderCodec.decode(valueType, decoder)


	object NonRecursive : AbstractJSONDecoderCodec<List<*>, JSONCodingContext>(
		additionalProviders = listOf(BooleanJSONCodec, IntJSONCodec, StringJSONCodec)
	) {

		override fun decode(valueType: JSONCodingType<in List<*>>, decoder: JSONDecoder<JSONCodingContext>) =
			ListJSONDecoderCodec.nonRecursive.decode(valueType, decoder)
	}
}
