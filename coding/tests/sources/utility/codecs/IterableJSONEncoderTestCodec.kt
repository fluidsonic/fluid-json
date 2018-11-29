package tests.coding

import com.github.fluidsonic.fluid.json.*


internal object IterableJSONEncoderTestCodec : AbstractJSONEncoderCodec<Iterable<*>, JSONCodingContext>(
	additionalProviders = listOf(AnyJSONDecoderCodec, BooleanJSONCodec, NumberJSONCodec, StringJSONCodec)
) {

	override fun encode(value: Iterable<*>, encoder: JSONEncoder<JSONCodingContext>) =
		IterableJSONEncoderCodec.encode(value, encoder)


	object NonRecursive : AbstractJSONEncoderCodec<Iterable<*>, JSONCodingContext>(
		additionalProviders = listOf(BooleanJSONCodec, IntJSONCodec, StringJSONCodec)
	) {

		override fun encode(value: Iterable<*>, encoder: JSONEncoder<JSONCodingContext>) =
			IterableJSONEncoderCodec.nonRecursive.encode(value, encoder)
	}
}
