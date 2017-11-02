package com.github.fluidsonic.fluid.json


internal class StandardDecoder<out Context : JSONCoderContext>(
	override val context: Context,
	private val codecProvider: JSONCodecProvider<Context>,
	source: JSONReader
) : JSONDecoder<Context>, JSONReader by source {

	override fun readValue() =
		super<JSONDecoder>.readValue()


	override fun <Value : Any> readValueOfType(valueType: JSONCodableType<in Value>) =
		codecProvider.decoderCodecForType(valueType)
			?.decode(valueType = valueType, decoder = this)
			?: throw JSONException("no decoder codec registered for $valueType")
}
