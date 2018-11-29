package tests.coding

import com.github.fluidsonic.fluid.json.*


internal object MapJSONTestCodec : AbstractJSONCodec<Map<*, *>, JSONCodingContext>(
	additionalProviders = listOf(AnyJSONDecoderCodec, BooleanJSONCodec, LocalDateCodec, NumberJSONCodec, StringJSONCodec)
) {

	override fun decode(valueType: JSONCodingType<in Map<*, *>>, decoder: JSONDecoder<JSONCodingContext>) =
		MapJSONCodec.decode(valueType, decoder)


	override fun encode(value: Map<*, *>, encoder: JSONEncoder<JSONCodingContext>) =
		MapJSONCodec.encode(value, encoder)


	object NonRecursive : AbstractJSONCodec<Map<String, *>, JSONCodingContext>(
		additionalProviders = listOf(BooleanJSONCodec, IntJSONCodec, StringJSONCodec)
	) {

		override fun decode(valueType: JSONCodingType<in Map<String, *>>, decoder: JSONDecoder<JSONCodingContext>) =
			MapJSONCodec.nonRecursive.decode(valueType, decoder)


		override fun encode(value: Map<String, *>, encoder: JSONEncoder<JSONCodingContext>) =
			MapJSONCodec.nonRecursive.encode(value, encoder)
	}
}
