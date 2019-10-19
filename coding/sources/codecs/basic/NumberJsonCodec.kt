package io.fluidsonic.json


object NumberJsonCodec : AbstractJsonCodec<Number, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Number>) =
		readNumber()


	override fun JsonEncoder<JsonCodingContext>.encode(value: Number) =
		writeNumber(value)
}
