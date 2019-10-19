package io.fluidsonic.json


object ShortJsonCodec : AbstractJsonCodec<Short, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Short>) =
		readShort()


	override fun JsonEncoder<JsonCodingContext>.encode(value: Short) =
		writeShort(value)
}
