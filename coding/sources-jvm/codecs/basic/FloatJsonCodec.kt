package io.fluidsonic.json


public object FloatJsonCodec : AbstractJsonCodec<Float, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Float>): Float =
		readFloat()


	override fun JsonEncoder<JsonCodingContext>.encode(value: Float): Unit =
		writeFloat(value)
}
