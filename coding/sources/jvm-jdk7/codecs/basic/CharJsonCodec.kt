package io.fluidsonic.json


public object CharJsonCodec : AbstractJsonCodec<Char, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Char>): Char =
		readChar()


	override fun JsonEncoder<JsonCodingContext>.encode(value: Char): Unit =
		writeChar(value)
}
