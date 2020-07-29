package io.fluidsonic.json


public object ShortJsonCodec : AbstractJsonCodec<Short, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Short>): Short =
		readShort()


	override fun JsonEncoder<JsonCodingContext>.encode(value: Short): Unit =
		writeShort(value)
}
