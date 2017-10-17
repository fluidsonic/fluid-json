package com.github.fluidsonic.fluid.json


internal class MapJSONCodec : JSONCodec<Map<*, *>, JSONCoderContext> {

	override fun decode(decoder: JSONDecoder<out JSONCoderContext>): Map<*, *> =
		mutableMapOf<String, Any?>().also { map ->
			decoder.readMapByEntry { map[it] = readValue() }
		}


	override fun encode(value: Map<*, *>, encoder: JSONEncoder<out JSONCoderContext>) {
		encoder.writeMap {
			for ((elementKey, elementValue) in value) {
				writeKey(elementKey as String) // FIXME EncodableKey?
				writeEncodableOrNull(elementValue)
			}
		}
	}
}
