package io.fluidsonic.json


public object ByteJsonCodec : AbstractJsonCodec<Byte, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Byte>): Byte =
		readByte()


	override fun JsonEncoder<JsonCodingContext>.encode(value: Byte): Unit =
		writeByte(value)
}
