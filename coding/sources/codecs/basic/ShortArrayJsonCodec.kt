package io.fluidsonic.json


object ShortArrayJsonCodec : AbstractJsonEncoderCodec<ShortArray, JsonCodingContext>() {

	override fun JsonEncoder<JsonCodingContext>.encode(value: ShortArray) =
		writeList(value)
}
