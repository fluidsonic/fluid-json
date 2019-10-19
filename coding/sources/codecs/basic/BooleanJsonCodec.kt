package io.fluidsonic.json


object BooleanJsonCodec : AbstractJsonCodec<Boolean, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Boolean>) =
		readBoolean()


	override fun JsonEncoder<JsonCodingContext>.encode(value: Boolean) =
		writeBoolean(value)
}
