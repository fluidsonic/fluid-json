package tests

import com.github.fluidsonic.fluid.json.*


internal fun <Value : Any> JSONDecoderCodec<Value, JSONCoderContext>.parse(source: String, type: JSONCodableType<Value>) =
	JSONParser.builder()
		.decodingWith(this, base = null)
		.build()
		.parseValueOfType(source, type)


internal fun <Value : Any> JSONEncoderCodec<Value, JSONCoderContext>.serialize(value: Value) =
	JSONSerializer.builder()
		.encodingWith(this, base = null)
		.build()
		.serializeValue(value)
