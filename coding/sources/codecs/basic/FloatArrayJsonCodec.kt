package io.fluidsonic.json


object FloatArrayJsonCodec : AbstractJsonEncoderCodec<FloatArray, JsonCodingContext>() {

	override fun JsonEncoder<JsonCodingContext>.encode(value: FloatArray) =
		writeList(value)
}
