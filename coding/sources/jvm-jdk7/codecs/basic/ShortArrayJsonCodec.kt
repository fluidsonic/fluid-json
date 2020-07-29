package io.fluidsonic.json


public object ShortArrayJsonCodec : AbstractJsonEncoderCodec<ShortArray, JsonCodingContext>() {

	override fun JsonEncoder<JsonCodingContext>.encode(value: ShortArray): Unit =
		writeList(value)
}
