package com.github.fluidsonic.fluid.json


object SequenceJSONCodec : AbstractJSONCodec<Sequence<*>, JSONCodingContext>() {

	override fun decode(valueType: JSONCodingType<in Sequence<*>>, decoder: JSONDecoder<JSONCodingContext>): Sequence<*> {
		val elementType = valueType.arguments.single()

		return decoder.readListByElement {
			readValueOfTypeOrNull(elementType)
		}.asSequence()
	}


	override fun encode(value: Sequence<*>, encoder: JSONEncoder<JSONCodingContext>) =
		encoder.writeList(value)


	val nonRecursive = NonRecursiveJSONCodec.create<Sequence<*>>()
}
