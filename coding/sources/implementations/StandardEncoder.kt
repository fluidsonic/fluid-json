package com.github.fluidsonic.fluid.json


internal class StandardEncoder<out Context : JSONCodingContext>(
	override val context: Context,
	private val codecProvider: JSONCodecProvider<Context>,
	private val destination: JSONWriter
) : JSONEncoder<Context>, JSONWriter by destination {

	override fun writeValue(value: Any) {
		withErrorChecking {
			codecProvider.encoderCodecForClass(value::class)
				?.run { encode(value = value) }
				?: throw JSONException("no encoder codec registered for ${value::class}: $value")
		}
	}
}
