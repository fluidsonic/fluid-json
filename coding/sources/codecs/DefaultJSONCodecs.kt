package com.github.fluidsonic.fluid.json

import com.github.fluidsonic.fluid.json.dynamic.*


object DefaultJSONCodecs {

	val basic: List<JSONCodecProvider<JSONCodingContext>> =
		listOf(
			AnyJSONDecoderCodec,
			ArrayJSONCodec,
			BooleanArrayJSONCodec,
			BooleanJSONCodec,
			ByteArrayJSONCodec,
			ByteJSONCodec,
			DoubleArrayJSONCodec,
			DoubleJSONCodec,
			FloatArrayJSONCodec,
			FloatJSONCodec,
			IntArrayJSONCodec,
			IntJSONCodec,
			ListJSONDecoderCodec,
			LongArrayJSONCodec,
			LongJSONCodec,
			MapJSONCodec,
			SequenceJSONCodec,
			ShortArrayJSONCodec,
			ShortJSONCodec,
			StringJSONCodec,
			IterableJSONEncoderCodec, // after subclasses
			NumberJSONCodec // after subclasses
		)


	val extended: List<JSONCodecProvider<JSONCodingContext>> =
		codingImplementationsJava.extendedCodecProviders()


	val nonRecursive: List<JSONCodecProvider<JSONCodingContext>> =
		listOf(
			ArrayJSONCodec.nonRecursive,
			ListJSONDecoderCodec.nonRecursive,
			MapJSONCodec.nonRecursive,
			SequenceJSONCodec.nonRecursive,
			IterableJSONEncoderCodec.nonRecursive // after subclasses
		)
}
