package io.fluidsonic.json


object ByteArrayJsonCodec : AbstractJsonEncoderCodec<ByteArray, JsonCodingContext>() {

	override fun JsonEncoder<JsonCodingContext>.encode(value: ByteArray) =
		writeList(value)
}
