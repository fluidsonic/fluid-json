package com.github.fluidsonic.fluid.json


class StandardDecoder<Context : JSONCoderContext>(
	override val context: Context,
	private val codecResolver: JSONCodecResolver<Context>,
	source: JSONReader
) : JSONDecoder<Context>, JSONReader by source {

	override fun <Value : Any> readDecodableOfClass(valueClass: Class<out Value>) =
		codecResolver.decoderCodecForClass(valueClass)
			?.decode(decoder = this)
			?: throw JSONException("no decoder codec registered for $valueClass")
}
