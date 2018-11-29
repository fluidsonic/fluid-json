package com.github.fluidsonic.fluid.json

import kotlin.reflect.KClass


internal class NonRecursiveJSONEncoderCodec<Value : Any>(
	override val encodableClass: KClass<Value>
) : JSONEncoderCodec<Value, JSONCodingContext> {

	override fun encode(value: Value, encoder: JSONEncoder<JSONCodingContext>) {
		JSONSerializer.default.serializeValue(value, destination = encoder, withTermination = false)
	}


	companion object {

		inline fun <reified Value : Any> create(): JSONEncoderCodec<Value, JSONCodingContext> =
			NonRecursiveJSONEncoderCodec(encodableClass = Value::class)
	}
}
