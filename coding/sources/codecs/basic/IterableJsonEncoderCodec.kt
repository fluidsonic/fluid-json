package io.fluidsonic.json


object IterableJsonEncoderCodec : AbstractJsonEncoderCodec<Iterable<*>, JsonCodingContext>() {

	override fun JsonEncoder<JsonCodingContext>.encode(value: Iterable<*>) =
		writeList(value)


	val nonRecursive = NonRecursiveJsonEncoderCodec.create<Iterable<*>>()
}
