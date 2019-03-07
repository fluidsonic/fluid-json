package tests.coding

import com.github.fluidsonic.fluid.json.AbstractJSONCodec
import com.github.fluidsonic.fluid.json.AnyJSONDecoderCodec
import com.github.fluidsonic.fluid.json.BooleanJSONCodec
import com.github.fluidsonic.fluid.json.IntJSONCodec
import com.github.fluidsonic.fluid.json.JSONCodingContext
import com.github.fluidsonic.fluid.json.JSONCodingType
import com.github.fluidsonic.fluid.json.JSONDecoder
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.MapJSONCodec
import com.github.fluidsonic.fluid.json.NumberJSONCodec
import com.github.fluidsonic.fluid.json.StringJSONCodec


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
