package com.github.fluidsonic.fluid.json

import kotlin.reflect.KClass


internal class NonRecursiveJSONEncoderCodec<Value : Any>(
	override val encodableClass: KClass<Value>
) : JSONEncoderCodec<Value, JSONCoderContext> {

	override fun encode(value: Value, encoder: JSONEncoder<out JSONCoderContext>) {
		NonRecursiveSerializer.serialize(value, destination = encoder)
	}


	companion object {

		inline fun <reified Value : Any> create(): JSONEncoderCodec<Value, JSONCoderContext> =
			NonRecursiveJSONEncoderCodec(encodableClass = Value::class)
	}
}
