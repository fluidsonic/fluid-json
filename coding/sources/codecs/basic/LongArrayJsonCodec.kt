package io.fluidsonic.json


object LongArrayJsonCodec : AbstractJsonEncoderCodec<LongArray, JsonCodingContext>() {

	override fun JsonEncoder<JsonCodingContext>.encode(value: LongArray) =
		writeList(value)
}
