package com.github.fluidsonic.fluid.json


internal class SimpleDecoder<out Context : JSONCoderContext>(
	private val codecResolver: JSONCodecResolver<Context>,
	override val context: Context,
	source: JSONReader
) : JSONDecoder<Context>, JSONReader by source {

	override fun <Value : Any> readDecodableOfClass(valueClass: Class<Value>) =
		codecResolver.decoderCodecForClass(valueClass)
			?.decode(decoder = this)
			?: throw JSONException("no decoder codec registered for $valueClass")
}
