package io.fluidsonic.json


public object StringJsonCodec : AbstractJsonCodec<String, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<String>): String =
		readString()


	override fun JsonEncoder<JsonCodingContext>.encode(value: String): Unit =
		writeString(value)
}
