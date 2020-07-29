package io.fluidsonic.json


@Suppress("UNCHECKED_CAST")
public class EnumJsonCodecProvider(
	public val transformation: EnumJsonTransformation
) : JsonCodecProvider<JsonCodingContext> by JsonCodecProvider.factoryOf<Enum<*>>({ enumClass ->
	EnumJsonCodec(
		enumClass = enumClass,
		transformation = transformation
	) as EnumJsonCodec<Enum<*>>
})
