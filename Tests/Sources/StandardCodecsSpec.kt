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


// FIXME
/*
describe("transforms invalid keys & values to string") {

	subject { StandardSerializer(convertsInvalidValuesToString = true, convertsInvalidKeysToString = true) }


	it("returns correct conversion settings") {
		StandardSerializer().convertsInvalidKeysToString.should.be.`false`
		StandardSerializer().convertsInvalidValuesToString.should.be.`false`
		StandardSerializer(convertsInvalidKeysToString = false).convertsInvalidKeysToString.should.be.`false`
		StandardSerializer(convertsInvalidValuesToString = false).convertsInvalidValuesToString.should.be.`false`
		StandardSerializer(convertsInvalidKeysToString = true).convertsInvalidKeysToString.should.be.`true`
		StandardSerializer(convertsInvalidValuesToString = true).convertsInvalidValuesToString.should.be.`true`
	}

	it("non-finite float") {
		subject.serialize(Float.NEGATIVE_INFINITY).should.equal("\"-Infinity\"")
		subject.serialize(Float.POSITIVE_INFINITY).should.equal("\"Infinity\"")
		subject.serialize(Float.NaN).should.equal("\"NaN\"")
	}

	it("non-finite double") {
		subject.serialize(Double.NEGATIVE_INFINITY).should.equal("\"-Infinity\"")
		subject.serialize(Double.POSITIVE_INFINITY).should.equal("\"Infinity\"")
		subject.serialize(Double.NaN).should.equal("\"NaN\"")
	}

	it("non-string map keys") {
		subject.serialize(mapOf(null to null)).should.equal("{\"null\":null}")
		subject.serialize(mapOf(0 to 0)).should.equal("{\"0\":0}")
	}

	it("unsupported classes") {
		val obj = object {
			override fun toString() = "object"
		}

		subject.serialize(obj).should.equal("\"object\"")
	}
}
*/
