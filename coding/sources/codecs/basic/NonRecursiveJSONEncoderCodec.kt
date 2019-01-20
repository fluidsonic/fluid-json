package com.github.fluidsonic.fluid.json

import kotlin.reflect.KClass


internal class NonRecursiveJSONEncoderCodec<Value : Any>(
	override val encodableClass: KClass<Value>
) : JSONEncoderCodec<Value, JSONCodingContext> {

	override fun JSONEncoder<JSONCodingContext>.encode(value: Value) {
		JSONSerializer.default.serializeValue(value, destination = this, withTermination = false)
	}


	companion object {

		inline fun <reified Value : Any> create(): JSONEncoderCodec<Value, JSONCodingContext> =
			NonRecursiveJSONEncoderCodec(encodableClass = Value::class)
	}
}
