package com.github.fluidsonic.fluid.json


internal class StandardEncoder<Context : JSONCoderContext>(
	override val context: Context,
	private val codecResolver: JSONCodecResolver<Context>,
	private val destination: JSONWriter
) : JSONEncoder<Context>, JSONWriter by destination {

	@Suppress("UNCHECKED_CAST")
	override fun writeEncodable(value: Any) {
		withErrorChecking {
			codecResolver.encoderCodecForClass(value::class.java as Class<Any>)
				?.encode(value = value, encoder = this)
				?: throw JSONException("no encoder codec registered for ${value::class.java}: $value")
		}
	}


	override fun writeValue(value: Any?) =
		super<JSONEncoder>.writeValue(value)


	override fun writeValueAsMapKey(value: Any?) =
		super<JSONEncoder>.writeValueAsMapKey(value)
}
