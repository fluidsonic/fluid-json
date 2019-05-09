package tests.coding

import com.github.fluidsonic.fluid.json.*


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
