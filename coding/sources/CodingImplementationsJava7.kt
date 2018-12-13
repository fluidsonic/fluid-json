package com.github.fluidsonic.fluid.json.dynamic

import com.github.fluidsonic.fluid.json.*


internal open class CodingImplementationsJava7 : CodingImplementationsJava {

	override fun extendedCodecProviders() =
		super.extendedCodecProviders() + listOf(
			// from specific to unspecific
			CharRangeJSONCodec,
			IntRangeJSONCodec,
			LongRangeJSONCodec,
			ClosedRangeJSONCodec
		)
}
