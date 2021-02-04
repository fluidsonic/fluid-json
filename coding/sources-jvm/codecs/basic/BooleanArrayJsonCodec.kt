package io.fluidsonic.json


public object BooleanArrayJsonCodec : AbstractJsonEncoderCodec<BooleanArray, JsonCodingContext>() {

	override fun JsonEncoder<JsonCodingContext>.encode(value: BooleanArray): Unit =
		writeList(value)
}
