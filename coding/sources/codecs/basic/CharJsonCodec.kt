package io.fluidsonic.json


object CharJsonCodec : AbstractJsonCodec<Char, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Char>) =
		readChar()


	override fun JsonEncoder<JsonCodingContext>.encode(value: Char) =
		writeChar(value)
}
