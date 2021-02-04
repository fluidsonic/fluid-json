package io.fluidsonic.json


public object ArrayJsonCodec : AbstractJsonEncoderCodec<Array<out Any?>, JsonCodingContext>() {

	override fun JsonEncoder<JsonCodingContext>.encode(value: Array<out Any?>): Unit =
		writeList(value)


	public val nonRecursive: JsonEncoderCodec<Array<out Any?>, JsonCodingContext> = NonRecursiveJsonEncoderCodec.create()
}
