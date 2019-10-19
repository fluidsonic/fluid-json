package io.fluidsonic.json


internal class StandardCodingSerializer<out Context : JsonCodingContext>(
	private val context: Context,
	private val encoderFactory: (destination: JsonWriter, context: Context) -> JsonEncoder<Context>
) : JsonCodingSerializer {

	override fun serializeValue(value: Any?, destination: JsonWriter, withTermination: Boolean) {
		encoderFactory(destination, context)
			.withTermination(withTermination) { writeValueOrNull(value) }
	}
}
