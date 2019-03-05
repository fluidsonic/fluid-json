package com.github.fluidsonic.fluid.json


object CollectionJSONCodec : AbstractJSONCodec<Collection<*>, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in Collection<*>>): Collection<*> =
		ListJSONDecoderCodec.run { decode(valueType) }


	override fun JSONEncoder<JSONCodingContext>.encode(value: Collection<*>): Unit =
		writeList(value)


	val nonRecursive: JSONCodec<Collection<*>, JSONCodingContext> = NonRecursiveJSONCodec.create()
}
