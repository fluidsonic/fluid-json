package tests.coding

import io.fluidsonic.json.*


internal fun <Value : Any> JsonDecoderCodec<Value, JsonCodingContext>.parse(source: String, type: JsonCodingType<Value>) =
	JsonCodingParser.builder()
		.decodingWith(this, base = null)
		.build()
		.parseValueOfType(source, type)


internal fun <Value : Any> JsonEncoderCodec<Value, JsonCodingContext>.serialize(value: Value) =
	JsonCodingSerializer.builder()
		.encodingWith(this, base = null)
		.build()
		.serializeValue(value)
