package io.fluidsonic.json


public object DoubleArrayJsonCodec : AbstractJsonEncoderCodec<DoubleArray, JsonCodingContext>() {

	override fun JsonEncoder<JsonCodingContext>.encode(value: DoubleArray): Unit =
		writeList(value)
}
