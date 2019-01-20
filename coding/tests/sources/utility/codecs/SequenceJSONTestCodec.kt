package tests.coding

import com.github.fluidsonic.fluid.json.AbstractJSONCodec
import com.github.fluidsonic.fluid.json.AnyJSONDecoderCodec
import com.github.fluidsonic.fluid.json.BooleanJSONCodec
import com.github.fluidsonic.fluid.json.IntJSONCodec
import com.github.fluidsonic.fluid.json.IterableJSONEncoderCodec
import com.github.fluidsonic.fluid.json.JSONCodingContext
import com.github.fluidsonic.fluid.json.JSONCodingType
import com.github.fluidsonic.fluid.json.JSONDecoder
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.ListJSONDecoderCodec
import com.github.fluidsonic.fluid.json.NumberJSONCodec
import com.github.fluidsonic.fluid.json.SequenceJSONCodec
import com.github.fluidsonic.fluid.json.StringJSONCodec


internal object SequenceJSONTestCodec : AbstractJSONCodec<Sequence<*>, JSONCodingContext>(
	additionalProviders = listOf(AnyJSONDecoderCodec, BooleanJSONCodec, IterableJSONEncoderCodec, ListJSONDecoderCodec, NumberJSONCodec, StringJSONCodec)
) {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in Sequence<*>>) =
		SequenceJSONCodec.run { decode(valueType) }


	override fun JSONEncoder<JSONCodingContext>.encode(value: Sequence<*>) =
		SequenceJSONCodec.run { encode(value) }


	object NonRecursive : AbstractJSONCodec<Sequence<*>, JSONCodingContext>(
		additionalProviders = listOf(BooleanJSONCodec, IntJSONCodec, IterableJSONEncoderCodec, StringJSONCodec)
	) {

		override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in Sequence<*>>) =
			SequenceJSONCodec.nonRecursive.run { decode(valueType) }


		override fun JSONEncoder<JSONCodingContext>.encode(value: Sequence<*>) =
			SequenceJSONCodec.nonRecursive.run { encode(value) }
	}
}
