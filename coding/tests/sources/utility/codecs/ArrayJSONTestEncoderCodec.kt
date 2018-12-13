package tests.coding

import com.github.fluidsonic.fluid.json.*


internal object ArrayJSONTestEncoderCodec : AbstractJSONEncoderCodec<Array<*>, JSONCodingContext>(
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
