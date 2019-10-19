package io.fluidsonic.json


internal abstract class NonRecursiveJsonDecoderCodec<Value : Any> : AbstractJsonDecoderCodec<Value, JsonCodingContext>() {

	private val expectedFirstToken = when (decodableType.rawClass) {
		Collection::class,
		Iterable::class,
		List::class,
		Sequence::class,
		Set::class ->
			JsonToken.listStart

		Map::class ->
			JsonToken.mapStart

		else -> error("Cannot decode $decodableType")
	}


	@Suppress("UNCHECKED_CAST")
	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Value>): Value {
		if (nextToken != expectedFirstToken) {
			throw JsonException.Schema(
				message = "Cannot decode $nextToken as $valueType",
				offset = offset,
				path = path
			)
		}

		val value = JsonParser.default.parseValueOrNull(this, withTermination = false)

		return when {
			Sequence::class.java.isAssignableFrom(valueType.rawClass.java) -> (value as Iterable<*>).asSequence() as Value
			Set::class.java.isAssignableFrom(valueType.rawClass.java) -> (value as Iterable<*>).toSet() as Value
			else -> value as Value
		}
	}


	companion object {

		inline fun <reified Value : Any> create(): JsonDecoderCodec<Value, JsonCodingContext> =
			object : NonRecursiveJsonDecoderCodec<Value>() {}
	}
}
