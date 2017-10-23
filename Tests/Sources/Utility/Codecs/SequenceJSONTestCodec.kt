package tests

import com.github.fluidsonic.fluid.json.BooleanJSONCodec
import com.github.fluidsonic.fluid.json.IntJSONCodec
import com.github.fluidsonic.fluid.json.IterableJSONCodec
import com.github.fluidsonic.fluid.json.JSONCodec
import com.github.fluidsonic.fluid.json.JSONCoderContext
import com.github.fluidsonic.fluid.json.SequenceJSONCodec
import com.github.fluidsonic.fluid.json.StringJSONCodec


internal object SequenceJSONTestCodec : JSONCodec<Sequence<*>, JSONCoderContext> by SequenceJSONCodec {

	override val codecs
		get() = SequenceJSONCodec.codecs + listOf(BooleanJSONCodec, IntJSONCodec, IterableJSONCodec, StringJSONCodec)

	override val decoderCodecs
		get() = super.decoderCodecs

	override val encoderCodecs
		get() = super.encoderCodecs
}
