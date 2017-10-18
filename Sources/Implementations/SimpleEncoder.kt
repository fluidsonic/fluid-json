package com.github.fluidsonic.fluid.json


internal class SimpleEncoder<out Context : JSONCoderContext>(
	private val codecResolver: JSONCodecResolver<Context>,
	override val context: Context,
	destination: JSONWriter
) : JSONEncoder<Context>, JSONWriter by destination {

	override fun writeEncodable(value: Any) {
		codecResolver.encoderCodecForClass(value::class.java)
			?.encode(value = value, encoder = this)
			?: throw JSONException("no encoder codec registered for ${value::class.java}: $value")
	}
}
