package tests.coding

import com.github.fluidsonic.fluid.json.*


internal object ListJSONTestDecoderCodec : AbstractJSONDecoderCodec<List<*>, JSONCodingContext>(
	additionalProviders = listOf(AnyJSONDecoderCodec, BooleanJSONCodec, NumberJSONCodec, StringJSONCodec)
) {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<List<*>>) =
		ListJSONDecoderCodec.run { decode(valueType) }


	object NonRecursive : AbstractJSONDecoderCodec<List<*>, JSONCodingContext>(
		additionalProviders = listOf(BooleanJSONCodec, IntJSONCodec, StringJSONCodec)
	) {

		override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<List<*>>) =
			ListJSONDecoderCodec.nonRecursive.run { decode(valueType) }
	}
}
