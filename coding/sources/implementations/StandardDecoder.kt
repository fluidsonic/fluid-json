package com.github.fluidsonic.fluid.json


internal class StandardDecoder<out Context : JSONCodingContext>(
	override val context: Context,
	private val codecProvider: JSONCodecProvider<Context>,
	source: JSONReader
) : JSONDecoder<Context>, JSONReader by source {

	override fun readValue() =
		super<JSONDecoder>.readValue()


	override fun <Value : Any> readValueOfType(valueType: JSONCodingType<Value>) =
		codecProvider.decoderCodecForType(valueType)
			?.run {
				try {
					isolateValueRead {
						decode(valueType = valueType)
					}
				}
				catch (e: JSONException) {
					// TODO remove .java once KT-28418 is fixed
					e.addSuppressed(JSONException.Parsing("… when decoding value of type $valueType using ${this::class.java.name}"))
					throw e
				}
			}
			?: throw JSONException.Parsing(
				message = "No decoder codec registered for $valueType",
				offset = offset,
				path = path
			)
}
