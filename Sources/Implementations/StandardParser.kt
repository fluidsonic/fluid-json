package com.github.fluidsonic.fluid.json

import java.io.Reader


internal class StandardParser<out Context : JSONCoderContext>(
	private val context: Context,
	private val decoderFactory: (source: Reader, context: Context) -> JSONDecoder<Context>
) : JSONParser {

	override fun <Value : Any> parseValueOfTypeOrNull(source: Reader, valueType: JSONCodableType<Value>) =
		decoderFactory(source, context).use { decoder ->
			val value = decoder.readValueOfTypeOrNull(valueType)
			decoder.readEndOfInput()

			value
		}
}
