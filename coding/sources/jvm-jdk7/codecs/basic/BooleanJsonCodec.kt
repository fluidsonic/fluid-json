package io.fluidsonic.json


public object BooleanJsonCodec : AbstractJsonCodec<Boolean, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Boolean>): Boolean =
		readBoolean()


	override fun JsonEncoder<JsonCodingContext>.encode(value: Boolean): Unit =
		writeBoolean(value)
}
