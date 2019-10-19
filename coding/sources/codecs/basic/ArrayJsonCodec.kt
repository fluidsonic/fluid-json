package io.fluidsonic.json


object ArrayJsonCodec : AbstractJsonEncoderCodec<Array<out Any?>, JsonCodingContext>() {

	override fun JsonEncoder<JsonCodingContext>.encode(value: Array<out Any?>) =
		writeList(value)


	val nonRecursive = NonRecursiveJsonEncoderCodec.create<Array<out Any?>>()
}
