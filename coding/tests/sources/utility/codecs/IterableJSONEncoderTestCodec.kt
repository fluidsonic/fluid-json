package tests.coding

import com.github.fluidsonic.fluid.json.AbstractJSONEncoderCodec
import com.github.fluidsonic.fluid.json.AnyJSONDecoderCodec
import com.github.fluidsonic.fluid.json.BooleanJSONCodec
import com.github.fluidsonic.fluid.json.IntJSONCodec
import com.github.fluidsonic.fluid.json.IterableJSONEncoderCodec
import com.github.fluidsonic.fluid.json.JSONCodingContext
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.NumberJSONCodec
import com.github.fluidsonic.fluid.json.StringJSONCodec


internal object IterableJSONEncoderTestCodec : AbstractJSONEncoderCodec<Iterable<*>, JSONCodingContext>(
	additionalProviders = listOf(AnyJSONDecoderCodec, BooleanJSONCodec, NumberJSONCodec, StringJSONCodec)
) {

	override fun JSONEncoder<JSONCodingContext>.encode(value: Iterable<*>) =
		IterableJSONEncoderCodec.run { encode(value) }


	object NonRecursive : AbstractJSONEncoderCodec<Iterable<*>, JSONCodingContext>(
		additionalProviders = listOf(BooleanJSONCodec, IntJSONCodec, StringJSONCodec)
	) {

		override fun JSONEncoder<JSONCodingContext>.encode(value: Iterable<*>) =
			IterableJSONEncoderCodec.nonRecursive.run { encode(value) }
	}
}
