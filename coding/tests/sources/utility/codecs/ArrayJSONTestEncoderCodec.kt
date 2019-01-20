package tests.coding

import com.github.fluidsonic.fluid.json.AbstractJSONEncoderCodec
import com.github.fluidsonic.fluid.json.ArrayJSONCodec
import com.github.fluidsonic.fluid.json.JSONCodingContext
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.StringJSONCodec


internal object ArrayJSONTestEncoderCodec : AbstractJSONEncoderCodec<Array<*>, JSONCodingContext>(
	additionalProviders = listOf(StringJSONCodec)
) {

	override fun JSONEncoder<JSONCodingContext>.encode(value: Array<*>) =
		ArrayJSONCodec.run { encode(value) }


	object NonRecursive : AbstractJSONEncoderCodec<Array<*>, JSONCodingContext>(
		additionalProviders = listOf(StringJSONCodec)
	) {

		override fun JSONEncoder<JSONCodingContext>.encode(value: Array<*>) =
			ArrayJSONCodec.nonRecursive.run { encode(value) }
	}
}
