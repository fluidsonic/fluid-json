package io.fluidsonic.json


public object LongArrayJsonCodec : AbstractJsonEncoderCodec<LongArray, JsonCodingContext>() {

	override fun JsonEncoder<JsonCodingContext>.encode(value: LongArray): Unit =
		writeList(value)
}
