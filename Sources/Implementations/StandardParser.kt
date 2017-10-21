package com.github.fluidsonic.fluid.json

import java.io.Reader


internal class StandardParser<in Context : JSONCoderContext>(
	private val decoderFactory: (source: Reader, context: Context) -> JSONDecoder<Context>
) : JSONParser<Context> {

	override fun <Value : Any> parse(source: Reader, valueClass: Class<out Value>, context: Context): Value? {
		val decoder = decoderFactory(source, context)
		return decoder.use {
			val value = decoder.readDecodableOfClassOrNull(valueClass)
			decoder.readEndOfInput()

			value
		}
	}
}
