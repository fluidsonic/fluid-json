package tests

import com.github.fluidsonic.fluid.json.BooleanJSONCodec
import com.github.fluidsonic.fluid.json.IntJSONCodec
import com.github.fluidsonic.fluid.json.JSONCodec
import com.github.fluidsonic.fluid.json.JSONCoderContext
import com.github.fluidsonic.fluid.json.MapJSONCodec
import com.github.fluidsonic.fluid.json.StringJSONCodec


internal object MapJSONTestCodec : JSONCodec<Map<*, *>, JSONCoderContext> by MapJSONCodec {

	override val codecs
		get() = MapJSONCodec.codecs + listOf(BooleanJSONCodec, IntJSONCodec, StringJSONCodec)

	override val decoderCodecs
		get() = super.decoderCodecs

	override val encoderCodecs
		get() = super.encoderCodecs
}
