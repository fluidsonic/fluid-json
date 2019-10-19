package io.fluidsonic.json


internal class StandardEncoder<out Context : JsonCodingContext>(
	override val context: Context,
	private val codecProvider: JsonCodecProvider<Context>,
	private val destination: JsonWriter
) : JsonEncoder<Context>, JsonWriter by destination {

	@Suppress("UNCHECKED_CAST")
	override fun writeValue(value: Any) {
		withErrorChecking {
			(codecProvider.encoderCodecForClass(value::class) as JsonEncoderCodec<Any, Context>?)
				?.run {
					try {
						isolateValueWrite {
							encode(value = value)
						}
					}
					catch (e: JsonException) {
						// TODO remove .java once KT-28418 is fixed
						e.addSuppressed(JsonException.Serialization("… when encoding value of ${value::class} using ${this::class.java.name}: $value"))
						throw e
					}
				}
				?: throw JsonException.Serialization(
					message = "No encoder codec registered for ${value::class}: $value",
					path = path
				)
		}
	}
}
