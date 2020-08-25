package io.fluidsonic.json


public object LongJsonCodec : AbstractJsonCodec<Long, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Long>): Long =
		readLong()


	override fun JsonEncoder<JsonCodingContext>.encode(value: Long): Unit =
		writeLong(value)
}
