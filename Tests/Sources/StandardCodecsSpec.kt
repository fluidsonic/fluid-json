package tests

import com.github.fluidsonic.fluid.json.BooleanArrayJSONCodec
import com.github.fluidsonic.fluid.json.BooleanJSONCodec
import com.github.fluidsonic.fluid.json.ByteArrayJSONCodec
import com.github.fluidsonic.fluid.json.ByteJSONCodec
import com.github.fluidsonic.fluid.json.DoubleArrayJSONCodec
import com.github.fluidsonic.fluid.json.DoubleJSONCodec
import com.github.fluidsonic.fluid.json.FloatArrayJSONCodec
import com.github.fluidsonic.fluid.json.FloatJSONCodec
import com.github.fluidsonic.fluid.json.IntArrayJSONCodec
import com.github.fluidsonic.fluid.json.IntJSONCodec
import com.github.fluidsonic.fluid.json.JSONCoderContext
import com.github.fluidsonic.fluid.json.JSONDecoderCodec
import com.github.fluidsonic.fluid.json.JSONEncoderCodec
import com.github.fluidsonic.fluid.json.LongArrayJSONCodec
import com.github.fluidsonic.fluid.json.LongJSONCodec
import com.github.fluidsonic.fluid.json.NumberJSONCodec
import com.github.fluidsonic.fluid.json.PlainJSONCodec
import com.github.fluidsonic.fluid.json.ShortArrayJSONCodec
import com.github.fluidsonic.fluid.json.ShortJSONCodec
import com.github.fluidsonic.fluid.json.StringJSONCodec
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it


@Suppress("UNCHECKED_CAST")
internal object StandardCodecsSpec : Spek({

	mapOf(
		ArrayJSONTestCodec to arrayData,
		BooleanArrayJSONCodec to booleanArrayData,
		BooleanJSONCodec to booleanData,
		ByteArrayJSONCodec to byteArrayData,
		ByteJSONCodec to byteData,
		DoubleArrayJSONCodec to doubleArrayData,
		DoubleJSONCodec to doubleData,
		FloatArrayJSONCodec to floatArrayData,
		FloatJSONCodec to floatData,
		IntArrayJSONCodec to intArrayData,
		IntJSONCodec to intData,
		IterableJSONTestCodec to iterableData,
		LongArrayJSONCodec to longArrayData,
		LongJSONCodec to longData,
		MapJSONTestCodec to mapData,
		NumberJSONCodec to numberData,
		PlainJSONCodec() to plainData,
		SequenceJSONTestCodec to sequenceData,
		ShortArrayJSONCodec to shortArrayData,
		ShortJSONCodec to shortData,
		StringJSONCodec to stringData
	)
		.forEach { (codec, testData) ->
			describe(codec::class.java.simpleName) {
				val decoderCodec = codec as? JSONDecoderCodec<*, JSONCoderContext>
				if (decoderCodec != null) {
					it("decodes a value") {
						try {
							testData.testDecoding(decoderCodec::parse)
						}
						catch (e: Throwable) {
							throw AssertionError("${codec::class.java.simpleName}: ${e.message}").apply {
								stackTrace = e.stackTrace
							}
						}
					}
				}

				codec as JSONEncoderCodec<Any, JSONCoderContext>

				it("encodes a value") {
					try {
						testData.testEncoding(codec::serialize)
					}
					catch (e: Throwable) {
						throw AssertionError("${codec::class.java.simpleName}: ${e.message}").apply {
							stackTrace = e.stackTrace
						}
					}
				}
			}
		}
})
