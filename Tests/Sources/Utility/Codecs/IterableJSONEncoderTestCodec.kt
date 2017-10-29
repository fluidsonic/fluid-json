package tests

import com.github.fluidsonic.fluid.json.*


internal object IterableJSONEncoderTestCodec : AbstractJSONEncoderCodec<Iterable<*>, JSONCoderContext>(
	additionalProviders = listOf(AnyJSONDecoderCodec, BooleanJSONCodec, NumberJSONCodec, StringJSONCodec)
) {

	override fun encode(value: Iterable<*>, encoder: JSONEncoder<out JSONCoderContext>) =
		IterableJSONEncoderCodec.encode(value, encoder)


	object NonRecursive : AbstractJSONEncoderCodec<Iterable<*>, JSONCoderContext>(
		additionalProviders = listOf(BooleanJSONCodec, IntJSONCodec, StringJSONCodec)
	) {

		override fun encode(value: Iterable<*>, encoder: JSONEncoder<out JSONCoderContext>) =
			IterableJSONEncoderCodec.nonRecursive.encode(value, encoder)
	}
}
