package io.fluidsonic.json


public object NumberJsonCodec : AbstractJsonCodec<Number, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Number>): Number =
		readNumber()


	override fun JsonEncoder<JsonCodingContext>.encode(value: Number): Unit =
		writeNumber(value)
}
