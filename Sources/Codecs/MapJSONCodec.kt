package com.github.fluidsonic.fluid.json


object MapJSONCodec : JSONCodec<Map<*, *>, JSONCoderContext> {

	override fun decode(decoder: JSONDecoder<out JSONCoderContext>): Map<*, *> =
		decoder.readMap(JSONNullability.Value)


	override fun encode(value: Map<*, *>, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeMap(value)


	override val decodableClass = Map::class
}
