package tests

import com.github.fluidsonic.fluid.json.*


internal fun <Value : Any> JSONDecoderCodec<Value, JSONCodingContext>.parse(source: String, type: JSONCodingType<Value>) =
	JSONCodingParser.builder()
		.decodingWith(this, base = null)
		.build()
		.parseValueOfType(source, type)


internal fun <Value : Any> JSONEncoderCodec<Value, JSONCodingContext>.serialize(value: Value) =
	JSONCodingSerializer.builder()
		.encodingWith(this, base = null)
		.build()
		.serializeValue(value)
