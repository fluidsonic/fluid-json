package tests.coding

import com.github.fluidsonic.fluid.json.AbstractJSONDecoderCodec
import com.github.fluidsonic.fluid.json.AnyJSONDecoderCodec
import com.github.fluidsonic.fluid.json.BooleanJSONCodec
import com.github.fluidsonic.fluid.json.JSONCodingContext
import com.github.fluidsonic.fluid.json.JSONCodingType
import com.github.fluidsonic.fluid.json.JSONDecoder
import com.github.fluidsonic.fluid.json.ListJSONDecoderCodec
import com.github.fluidsonic.fluid.json.MapJSONCodec
import com.github.fluidsonic.fluid.json.NumberJSONCodec
import com.github.fluidsonic.fluid.json.StringJSONCodec


internal object AnyJSONTestDecoderCodec : AbstractJSONDecoderCodec<Any, JSONCodingContext>(
	additionalProviders = listOf(BooleanJSONCodec, ListJSONDecoderCodec, MapJSONCodec, NumberJSONCodec, StringJSONCodec)
) {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in Any>) =
		AnyJSONDecoderCodec.run { decode(valueType) }
}
