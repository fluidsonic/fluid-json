package io.fluidsonic.json


public object CharArrayJsonCodec : AbstractJsonEncoderCodec<CharArray, JsonCodingContext>() {

	override fun JsonEncoder<JsonCodingContext>.encode(value: CharArray): Unit =
		writeList(value)
}
