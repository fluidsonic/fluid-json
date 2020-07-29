package io.fluidsonic.json.dynamic

import io.fluidsonic.json.*


internal open class CodingImplementationsJava7 : CodingImplementationsJava {

	override fun extendedCodecProviders() =
		super.extendedCodecProviders() + listOf(
			// from specific to unspecific
			CharRangeJsonCodec,
			IntRangeJsonCodec,
			LongRangeJsonCodec,
			ClosedRangeJsonCodec,
			EnumJsonCodecProvider(
				transformation = EnumJsonTransformation.ToString(case = EnumJsonTransformation.Case.lowerCamelCase)
			)
		)
}
