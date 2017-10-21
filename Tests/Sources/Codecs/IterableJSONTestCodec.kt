package tests

import com.github.fluidsonic.fluid.json.BooleanJSONCodec
import com.github.fluidsonic.fluid.json.IntJSONCodec
import com.github.fluidsonic.fluid.json.IterableJSONCodec
import com.github.fluidsonic.fluid.json.JSONCodec
import com.github.fluidsonic.fluid.json.JSONCoderContext
import com.github.fluidsonic.fluid.json.StringJSONCodec


internal object IterableJSONTestCodec : JSONCodec<Iterable<*>, JSONCoderContext> by IterableJSONCodec {

	override val codecs
		get() = IterableJSONCodec.codecs + listOf(BooleanJSONCodec, IntJSONCodec, StringJSONCodec)

	override val decoderCodecs
		get() = super.decoderCodecs

	override val encoderCodecs
		get() = super.encoderCodecs
}
