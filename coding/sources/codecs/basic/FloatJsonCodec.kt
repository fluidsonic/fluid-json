package io.fluidsonic.json


object FloatJsonCodec : AbstractJsonCodec<Float, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Float>) =
		readFloat()


	override fun JsonEncoder<JsonCodingContext>.encode(value: Float) =
		writeFloat(value)
}
