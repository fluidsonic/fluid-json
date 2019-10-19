package io.fluidsonic.json


object IntJsonCodec : AbstractJsonCodec<Int, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Int>) =
		readInt()


	override fun JsonEncoder<JsonCodingContext>.encode(value: Int) =
		writeInt(value)
}
