package tests

import com.github.fluidsonic.fluid.json.*


internal object AnyJSONTestDecoderCodec : AbstractJSONDecoderCodec<Any, JSONCoderContext>(
	additionalProviders = listOf(BooleanJSONCodec, ListJSONDecoderCodec, MapJSONCodec, NumberJSONCodec, StringJSONCodec)
) {

	override fun decode(valueType: JSONCodableType<in Any>, decoder: JSONDecoder<JSONCoderContext>) =
		AnyJSONDecoderCodec.decode(valueType, decoder)
}
