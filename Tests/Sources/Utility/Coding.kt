package tests

import com.github.fluidsonic.fluid.json.JSONCodecResolver
import com.github.fluidsonic.fluid.json.JSONCoderContext
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.JSONEncoderCodec
import com.github.fluidsonic.fluid.json.JSONSerializer
import com.github.fluidsonic.fluid.json.serialize
import java.io.StringWriter


internal fun <Value : Any> JSONEncoderCodec<Value, JSONCoderContext>.serialize(value: Value) =
	StringWriter().apply {
		JSONSerializer.default().serialize(
			value,
			destination = JSONEncoder.with(
				destination = this,
				codecResolver = JSONCodecResolver.of(this@serialize, appendDefaultCodecs = false)
			)
		)
	}.toString()
