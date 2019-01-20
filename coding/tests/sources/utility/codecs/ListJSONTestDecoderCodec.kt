package tests.coding

import com.github.fluidsonic.fluid.json.AbstractJSONDecoderCodec
import com.github.fluidsonic.fluid.json.AnyJSONDecoderCodec
import com.github.fluidsonic.fluid.json.BooleanJSONCodec
import com.github.fluidsonic.fluid.json.IntJSONCodec
import com.github.fluidsonic.fluid.json.JSONCodingContext
import com.github.fluidsonic.fluid.json.JSONCodingType
import com.github.fluidsonic.fluid.json.JSONDecoder
import com.github.fluidsonic.fluid.json.ListJSONDecoderCodec
import com.github.fluidsonic.fluid.json.NumberJSONCodec
import com.github.fluidsonic.fluid.json.StringJSONCodec


internal object ListJSONTestDecoderCodec : AbstractJSONDecoderCodec<List<*>, JSONCodingContext>(
	additionalProviders = listOf(AnyJSONDecoderCodec, BooleanJSONCodec, NumberJSONCodec, StringJSONCodec)
) {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in List<*>>) =
		ListJSONDecoderCodec.run { decode(valueType) }


	object NonRecursive : AbstractJSONDecoderCodec<List<*>, JSONCodingContext>(
		additionalProviders = listOf(BooleanJSONCodec, IntJSONCodec, StringJSONCodec)
	) {

		override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in List<*>>) =
			ListJSONDecoderCodec.nonRecursive.run { decode(valueType) }
	}
}
