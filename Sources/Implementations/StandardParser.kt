package com.github.fluidsonic.fluid.json

import java.io.Reader


internal class StandardParser<Context : JSONCoderContext>(
	override val context: Context,
	private val decoderFactory: (source: Reader, context: Context) -> JSONDecoder<in Context>
) : JSONParser<Context> {

	override fun <Value : Any> parseValueOfTypeOrNull(source: Reader, valueType: JSONCodableType<Value>) =
		decoderFactory(source, context).use { decoder ->
			val value = decoder.readValueOfTypeOrNull(valueType)
			decoder.readEndOfInput()

			value
		}


	override fun <NewContext : Context> withContext(context: NewContext) =
		StandardParser(context = context, decoderFactory = decoderFactory)
}
