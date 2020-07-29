package io.fluidsonic.json


public object IntArrayJsonCodec : AbstractJsonEncoderCodec<IntArray, JsonCodingContext>() {

	override fun JsonEncoder<JsonCodingContext>.encode(value: IntArray): Unit =
		writeList(value)
}
