package io.fluidsonic.json


object ByteJsonCodec : AbstractJsonCodec<Byte, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Byte>) =
		readByte()


	override fun JsonEncoder<JsonCodingContext>.encode(value: Byte) =
		writeByte(value)
}
