package io.fluidsonic.json


object BooleanArrayJsonCodec : AbstractJsonEncoderCodec<BooleanArray, JsonCodingContext>() {

	override fun JsonEncoder<JsonCodingContext>.encode(value: BooleanArray) =
		writeList(value)
}
