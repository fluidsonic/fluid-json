package io.fluidsonic.json


public object IntJsonCodec : AbstractJsonCodec<Int, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Int>): Int =
		readInt()


	override fun JsonEncoder<JsonCodingContext>.encode(value: Int): Unit =
		writeInt(value)
}
