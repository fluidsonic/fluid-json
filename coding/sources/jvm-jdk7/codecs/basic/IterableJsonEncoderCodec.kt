package io.fluidsonic.json


public object IterableJsonEncoderCodec : AbstractJsonEncoderCodec<Iterable<*>, JsonCodingContext>() {

	override fun JsonEncoder<JsonCodingContext>.encode(value: Iterable<*>): Unit =
		writeList(value)


	public val nonRecursive: JsonEncoderCodec<Iterable<*>, JsonCodingContext> = NonRecursiveJsonEncoderCodec.create()
}
