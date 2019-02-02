package com.github.fluidsonic.fluid.json


@Suppress("UNCHECKED_CAST")
class EnumJSONCodecProvider(
	val transformation: EnumJSONTransformation
) : JSONCodecProvider<JSONCodingContext> by JSONCodecProvider.factoryOf<Enum<*>>({ enumClass ->
	EnumJSONCodec(
		enumClass = enumClass,
		transformation = transformation
	) as EnumJSONCodec<Enum<*>>
})
