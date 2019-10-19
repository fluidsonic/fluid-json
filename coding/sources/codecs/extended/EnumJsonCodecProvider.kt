package io.fluidsonic.json


@Suppress("UNCHECKED_CAST")
class EnumJsonCodecProvider(
	val transformation: EnumJsonTransformation
) : JsonCodecProvider<JsonCodingContext> by JsonCodecProvider.factoryOf<Enum<*>>({ enumClass ->
	EnumJsonCodec(
		enumClass = enumClass,
		transformation = transformation
	) as EnumJsonCodec<Enum<*>>
})
