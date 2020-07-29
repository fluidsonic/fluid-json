package io.fluidsonic.json


public object ByteArrayJsonCodec : AbstractJsonEncoderCodec<ByteArray, JsonCodingContext>() {

	override fun JsonEncoder<JsonCodingContext>.encode(value: ByteArray): Unit =
		writeList(value)
}
