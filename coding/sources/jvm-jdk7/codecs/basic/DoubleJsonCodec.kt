package io.fluidsonic.json


public object DoubleJsonCodec : AbstractJsonCodec<Double, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Double>): Double =
		readDouble()


	override fun JsonEncoder<JsonCodingContext>.encode(value: Double): Unit =
		writeDouble(value)
}
