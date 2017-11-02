package tests

import com.github.fluidsonic.fluid.json.*


internal object ArrayJSONTestCodec : AbstractJSONEncoderCodec<Array<*>, JSONCoderContext>(
	additionalProviders = listOf(StringJSONCodec)
) {

	override fun encode(value: Array<*>, encoder: JSONEncoder<JSONCoderContext>) =
		ArrayJSONCodec.encode(value, encoder)


	object NonRecursive : AbstractJSONEncoderCodec<Array<*>, JSONCoderContext>(
		additionalProviders = listOf(StringJSONCodec)
	) {

		override fun encode(value: Array<*>, encoder: JSONEncoder<JSONCoderContext>) =
			ArrayJSONCodec.nonRecursive.encode(value, encoder)
	}
}
