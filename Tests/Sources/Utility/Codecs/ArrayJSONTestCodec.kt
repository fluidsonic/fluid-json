package tests

import com.github.fluidsonic.fluid.json.*


internal object ArrayJSONTestCodec : AbstractJSONEncoderCodec<Array<*>, JSONCodingContext>(
	additionalProviders = listOf(StringJSONCodec)
) {

	override fun encode(value: Array<*>, encoder: JSONEncoder<JSONCodingContext>) =
		ArrayJSONCodec.encode(value, encoder)


	object NonRecursive : AbstractJSONEncoderCodec<Array<*>, JSONCodingContext>(
		additionalProviders = listOf(StringJSONCodec)
	) {

		override fun encode(value: Array<*>, encoder: JSONEncoder<JSONCodingContext>) =
			ArrayJSONCodec.nonRecursive.encode(value, encoder)
	}
}
