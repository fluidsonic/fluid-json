package com.github.fluidsonic.fluid.json


internal class StandardEncoder<out Context : JSONCodingContext>(
	override val context: Context,
	private val codecProvider: JSONCodecProvider<Context>,
	private val destination: JSONWriter
) : JSONEncoder<Context>, JSONWriter by destination {

	override fun writeValue(value: Any) {
		withErrorChecking {
			codecProvider.encoderCodecForClass(value::class)
				?.run {
					try {
						isolateValueWrite {
							encode(value = value)
						}
					}
					catch (e: JSONException) {
						// TODO remove .java once KT-28418 is fixed
						e.addSuppressed(JSONException.Serialization("… when encoding value of ${value::class} using ${this::class.java.name}: $value"))
						throw e
					}
				}
				?: throw JSONException.Serialization(
					message = "No encoder codec registered for ${value::class}: $value",
					path = path
				)
		}
	}
}
