package io.fluidsonic.json


internal class StandardDecoder<out Context : JsonCodingContext>(
	override val context: Context,
	private val codecProvider: JsonCodecProvider<Context>,
	source: JsonReader
) : JsonDecoder<Context>, JsonReader by source {

	override fun readValue() =
		super<JsonDecoder>.readValue()


	override fun <Value : Any> readValueOfType(valueType: JsonCodingType<Value>) =
		codecProvider.decoderCodecForType(valueType)
			?.run {
				try {
					isolateValueRead {
						decode(valueType = valueType)
					}
				}
				catch (e: JsonException) {
					// TODO remove .java once KT-28418 is fixed
					e.addSuppressed(JsonException.Parsing("… when decoding value of type $valueType using ${this::class.java.name}"))
					throw e
				}
			}
			?: throw JsonException.Parsing(
				message = "No decoder codec registered for $valueType",
				offset = offset,
				path = path
			)
}
