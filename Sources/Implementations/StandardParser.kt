package com.github.fluidsonic.fluid.json


internal class StandardParser<in Context : JSONCoderContext>(
	private val decoderFactory: (source: JSONReader, context: Context) -> JSONDecoder<Context>
) : JSONParser<Context> {

	override fun parse(source: JSONReader, context: Context): Any? {
		val decoder = decoderFactory(source, context)
		decoder.use {
			val value = decoder.readDecodableOrNull<Any>()
			if (decoder.nextToken != null) {
				throw JSONException("decoder is messed up")
			}

			return value
		}
	}
}
