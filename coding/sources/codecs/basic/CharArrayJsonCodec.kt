package io.fluidsonic.json


object CharArrayJsonCodec : AbstractJsonEncoderCodec<CharArray, JsonCodingContext>() {

	override fun JsonEncoder<JsonCodingContext>.encode(value: CharArray) =
		writeList(value)
}
