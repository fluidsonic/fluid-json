package com.github.fluidsonic.fluid.json


object MapJSONCodec : JSONCodec<Map<*, *>, JSONCoderContext> {

	override fun decode(decoder: JSONDecoder<JSONCoderContext>): Map<*, *> =
		decoder.readMap()


	override fun encode(value: Map<*, *>, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeMap(value)


	override val valueClass = Map::class.java
}
