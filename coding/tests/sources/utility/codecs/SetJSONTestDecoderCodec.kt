package tests.coding

import com.github.fluidsonic.fluid.json.*


internal object SetJSONTestDecoderCodec : AbstractJSONDecoderCodec<Set<*>, JSONCodingContext>(
	additionalProviders = listOf(AnyJSONDecoderCodec, BooleanJSONCodec, ListJSONDecoderCodec, NumberJSONCodec, StringJSONCodec)
) {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<Set<*>>) =
		SetJSONDecoderCodec.run { decode(valueType) }


	object NonRecursive : AbstractJSONDecoderCodec<Set<*>, JSONCodingContext>(
		additionalProviders = listOf(BooleanJSONCodec, NumberJSONCodec, StringJSONCodec)
	) {

		override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<Set<*>>) =
			SetJSONDecoderCodec.nonRecursive.run { decode(valueType) }
	}
}
