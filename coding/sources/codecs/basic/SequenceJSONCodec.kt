package com.github.fluidsonic.fluid.json


object SequenceJSONCodec : AbstractJSONCodec<Sequence<*>, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in Sequence<*>>): Sequence<*> {
		val elementType = valueType.arguments.single()

		return readListByElement {
			readValueOfTypeOrNull(elementType)
		}.asSequence()
	}


	override fun JSONEncoder<JSONCodingContext>.encode(value: Sequence<*>) =
		writeList(value)


	val nonRecursive = NonRecursiveJSONCodec.create<Sequence<*>>()
}
