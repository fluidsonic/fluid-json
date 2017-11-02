package tests

import com.github.fluidsonic.fluid.json.*


internal fun <Value : Any> JSONDecoderCodec<Value, JSONCoderContext>.parse(source: String, type: JSONCodableType<Value>): Value? =
	JSONParser.builder()
		.decodingWith(this, appendBasic = false)
		.build()
		.parseValueOfType(source, type)


internal fun <Value : Any> JSONEncoderCodec<Value, JSONCoderContext>.serialize(value: Value) =
	JSONSerializer.builder()
		.encodingWith(this, appendBasic = false)
		.build()
		.serializeValue(value)
