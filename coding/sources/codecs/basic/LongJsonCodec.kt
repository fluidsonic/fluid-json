package io.fluidsonic.json


object LongJsonCodec : AbstractJsonCodec<Long, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Long>) =
		readLong()


	override fun JsonEncoder<JsonCodingContext>.encode(value: Long) =
		writeLong(value)
}
