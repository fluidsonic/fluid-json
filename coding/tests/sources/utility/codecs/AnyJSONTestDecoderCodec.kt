package tests.coding

import com.github.fluidsonic.fluid.json.*


internal object AnyJSONTestDecoderCodec : AbstractJSONDecoderCodec<Any, JSONCodingContext>(
	additionalProviders = listOf(BooleanJSONCodec, ListJSONDecoderCodec, MapJSONCodec, NumberJSONCodec, StringJSONCodec)
) {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<Any>) =
		AnyJSONDecoderCodec.run { decode(valueType) }
}
