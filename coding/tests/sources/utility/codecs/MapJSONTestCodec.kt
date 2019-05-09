package tests.coding

import com.github.fluidsonic.fluid.json.*


internal object MapJSONTestCodec : AbstractJSONCodec<Map<*, *>, JSONCodingContext>(
	additionalProviders = listOf(AnyJSONDecoderCodec, BooleanJSONCodec, NumberJSONCodec, StringJSONCodec, YearMonthDayCodec)
) {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<Map<*, *>>) =
		MapJSONCodec.run { decode(valueType) }


	override fun JSONEncoder<JSONCodingContext>.encode(value: Map<*, *>) =
		MapJSONCodec.run { encode(value) }


	object NonRecursive : AbstractJSONCodec<Map<String, *>, JSONCodingContext>(
		additionalProviders = listOf(BooleanJSONCodec, IntJSONCodec, StringJSONCodec)
	) {

		override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<Map<String, *>>) =
			MapJSONCodec.nonRecursive.run { decode(valueType) }


		override fun JSONEncoder<JSONCodingContext>.encode(value: Map<String, *>) =
			MapJSONCodec.nonRecursive.run { encode(value) }
	}
}
