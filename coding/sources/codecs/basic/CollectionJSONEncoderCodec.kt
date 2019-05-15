package com.github.fluidsonic.fluid.json


object CollectionJSONCodec : AbstractJSONCodec<Collection<*>, JSONCodingContext>() {

	@Suppress("UNCHECKED_CAST")
	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<Collection<*>>): Collection<*> =
		ListJSONDecoderCodec.run { decode(valueType as JSONCodingType<List<*>>) }


	override fun JSONEncoder<JSONCodingContext>.encode(value: Collection<*>): Unit =
		writeList(value)


	val nonRecursive: JSONCodec<Collection<*>, JSONCodingContext> = NonRecursiveJSONCodec.create()
}
