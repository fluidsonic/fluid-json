package tests

import com.github.fluidsonic.fluid.json.ArrayJSONCodec
import com.github.fluidsonic.fluid.json.JSONCoderContext
import com.github.fluidsonic.fluid.json.JSONEncoderCodec
import com.github.fluidsonic.fluid.json.StringJSONCodec


internal object ArrayJSONTestCodec : JSONEncoderCodec<Array<*>, JSONCoderContext> by ArrayJSONCodec {

	override val encoderCodecs
		get() = ArrayJSONCodec.encoderCodecs + StringJSONCodec
}
