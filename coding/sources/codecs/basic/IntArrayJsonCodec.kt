package io.fluidsonic.json


object IntArrayJsonCodec : AbstractJsonEncoderCodec<IntArray, JsonCodingContext>() {

	override fun JsonEncoder<JsonCodingContext>.encode(value: IntArray) =
		writeList(value)
}
