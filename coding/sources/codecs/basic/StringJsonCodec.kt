package io.fluidsonic.json


object StringJsonCodec : AbstractJsonCodec<String, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<String>) =
		readString()


	override fun JsonEncoder<JsonCodingContext>.encode(value: String) =
		writeString(value)
}
