package io.fluidsonic.json


public object FloatArrayJsonCodec : AbstractJsonEncoderCodec<FloatArray, JsonCodingContext>() {

	override fun JsonEncoder<JsonCodingContext>.encode(value: FloatArray): Unit =
		writeList(value)
}
