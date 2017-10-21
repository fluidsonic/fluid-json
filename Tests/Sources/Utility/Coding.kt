package tests

import com.github.fluidsonic.fluid.json.JSONCodecResolver
import com.github.fluidsonic.fluid.json.JSONCoderContext
import com.github.fluidsonic.fluid.json.JSONDecoderCodec
import com.github.fluidsonic.fluid.json.JSONEncoderCodec
import com.github.fluidsonic.fluid.json.JSONParser
import com.github.fluidsonic.fluid.json.JSONSerializer
import com.github.fluidsonic.fluid.json.parse
import com.github.fluidsonic.fluid.json.serialize


internal fun <Value : Any> JSONDecoderCodec<Value, JSONCoderContext>.parse(source: String): Value? =
	JSONParser.with(codecResolver = JSONCodecResolver.of(this, appendDefaultCodecs = false))
		.parse(source, valueClass)


internal fun <Value : Any> JSONEncoderCodec<Value, JSONCoderContext>.serialize(value: Value) =
	JSONSerializer.with(codecResolver = JSONCodecResolver.of(this, appendDefaultCodecs = false))
		.serialize(value)
