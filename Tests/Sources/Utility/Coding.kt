package tests

import com.github.fluidsonic.fluid.json.JSONCoderContext
import com.github.fluidsonic.fluid.json.JSONDecoderCodec
import com.github.fluidsonic.fluid.json.JSONEncoderCodec
import com.github.fluidsonic.fluid.json.JSONParser
import com.github.fluidsonic.fluid.json.JSONSerializer
import com.github.fluidsonic.fluid.json.doParseWithClass
import com.github.fluidsonic.fluid.json.serialize


internal inline fun <reified Value : Any> JSONDecoderCodec<Value, JSONCoderContext>.parse(source: String): Value? =
	JSONParser.builder()
		.decoder(this, appendDefaultCodecs = false)
		.build()
		.doParseWithClass(source, Value::class.java)


internal fun <Value : Any> JSONEncoderCodec<Value, JSONCoderContext>.serialize(value: Value) =
	JSONSerializer.builder()
		.encoder(this, appendDefaultCodecs = false)
		.build()
		.serialize(value)