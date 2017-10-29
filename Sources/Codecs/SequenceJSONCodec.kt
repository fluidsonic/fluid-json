package com.github.fluidsonic.fluid.json


object SequenceJSONCodec : AbstractJSONCodec<Sequence<*>, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in Sequence<*>>, decoder: JSONDecoder<out JSONCoderContext>): Sequence<*> {
		val valueType = valueType.arguments.single()

		return decoder.readListByElement {
			readValueOfTypeOrNull(valueType)
		}.asSequence()
	}


	override fun encode(value: Sequence<*>, encoder: JSONEncoder<out JSONCoderContext>) =
		encoder.writeList(value)


	val nonRecursive = NonRecursiveJSONCodec.create<Sequence<*>>()
}
