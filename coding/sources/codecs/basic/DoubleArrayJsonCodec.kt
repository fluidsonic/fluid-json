package io.fluidsonic.json


object DoubleArrayJsonCodec : AbstractJsonEncoderCodec<DoubleArray, JsonCodingContext>() {

	override fun JsonEncoder<JsonCodingContext>.encode(value: DoubleArray) =
		writeList(value)
}
