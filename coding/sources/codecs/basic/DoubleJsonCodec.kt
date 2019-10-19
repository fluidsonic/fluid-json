package io.fluidsonic.json


object DoubleJsonCodec : AbstractJsonCodec<Double, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Double>) =
		readDouble()


	override fun JsonEncoder<JsonCodingContext>.encode(value: Double) =
		writeDouble(value)
}
