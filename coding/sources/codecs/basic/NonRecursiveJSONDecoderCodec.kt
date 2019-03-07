package com.github.fluidsonic.fluid.json


internal abstract class NonRecursiveJSONDecoderCodec<Value : Any> : AbstractJSONDecoderCodec<Value, JSONCodingContext>() {

	private val expectedFirstToken = when (decodableType.rawClass) {
		Collection::class,
		Iterable::class,
		List::class,
		Sequence::class,
		Set::class ->
			JSONToken.listStart

		Map::class ->
			JSONToken.mapStart

		else -> error("Cannot decode $decodableType")
	}


	@Suppress("UNCHECKED_CAST")
	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<Value>): Value {
		if (nextToken != expectedFirstToken) {
			throw JSONException.Schema(
				message = "Cannot decode $nextToken as $valueType",
				offset = offset,
				path = path
			)
		}

		val value = JSONParser.default.parseValueOrNull(this, withTermination = false)

		return when {
			Sequence::class.java.isAssignableFrom(valueType.rawClass.java) -> (value as Iterable<*>).asSequence() as Value
			Set::class.java.isAssignableFrom(valueType.rawClass.java) -> (value as Iterable<*>).toSet() as Value
			else -> value as Value
		}
	}


	companion object {

		inline fun <reified Value : Any> create(): JSONDecoderCodec<Value, JSONCodingContext> =
			object : NonRecursiveJSONDecoderCodec<Value>() {}
	}
}
