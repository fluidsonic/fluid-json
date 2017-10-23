package com.github.fluidsonic.fluid.json

import kotlin.reflect.KClass


class StandardDecoder<Context : JSONCoderContext>(
	override val context: Context,
	private val codecResolver: JSONCodecResolver<Context>,
	source: JSONReader
) : JSONDecoder<Context>, JSONReader by source {

	override fun <Value : Any> readDecodableOfClass(valueClass: KClass<out Value>) =
		codecResolver.decoderCodecForClass(valueClass)
			?.decode(decoder = this)
			?: throw JSONException("no decoder codec registered for $valueClass")
}
