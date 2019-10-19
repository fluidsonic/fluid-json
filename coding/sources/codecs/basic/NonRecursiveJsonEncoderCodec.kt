package io.fluidsonic.json

import kotlin.reflect.*


internal class NonRecursiveJsonEncoderCodec<Value : Any>(
	override val encodableClass: KClass<Value>
) : JsonEncoderCodec<Value, JsonCodingContext> {

	override fun JsonEncoder<JsonCodingContext>.encode(value: Value) {
		JsonSerializer.default.serializeValue(value, destination = this, withTermination = false)
	}


	companion object {

		inline fun <reified Value : Any> create(): JsonEncoderCodec<Value, JsonCodingContext> =
			NonRecursiveJsonEncoderCodec(encodableClass = Value::class)
	}
}
