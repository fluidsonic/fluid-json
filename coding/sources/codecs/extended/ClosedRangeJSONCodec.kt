package com.github.fluidsonic.fluid.json


object ClosedRangeJSONCodec : AbstractJSONCodec<ClosedRange<*>, JSONCodingContext>() {

	override fun decode(valueType: JSONCodingType<in ClosedRange<*>>, decoder: JSONDecoder<JSONCodingContext>): ClosedRange<*> {
		@Suppress("UNCHECKED_CAST")
		val elementType = valueType.arguments.single() as JSONCodingType<Comparable<Any>>

		return when (elementType.rawClass) {
			Char::class -> decodeChar(decoder = decoder)
			Comparable::class -> decodeComparable(decoder = decoder)
			Double::class -> decodeDouble(decoder = decoder)
			Float::class -> decodeFloat(decoder = decoder)
			Int::class -> decodeInt(decoder = decoder)
			Long::class -> decodeLong(decoder = decoder)
			else -> decodeOther(elementType = elementType, decoder = decoder)
		}
	}


	@Suppress("UNCHECKED_CAST")
	private fun decodeComparable(decoder: JSONDecoder<JSONCodingContext>): ClosedRange<*> {
		var endInclusive: Comparable<Any>? = null
		var start: Comparable<Any>? = null

		decoder.readFromMapByElementValue { key ->
			when (key) {
				Fields.endInclusive -> endInclusive = readValue() as? Comparable<Any>
					?: throw JSONException("expected comparable value for '$key'")

				Fields.start -> start = readValue() as? Comparable<Any>
					?: throw JSONException("expected comparable value for '$key'")

				else -> skipValue()
			}
		}

		return (start ?: throw JSONException("missing '${Fields.start}'")) ..
			(endInclusive ?: throw JSONException("missing '${Fields.endInclusive}'"))
	}


	private fun decodeChar(decoder: JSONDecoder<JSONCodingContext>) =
		CharRangeJSONCodec.decode(valueType = CharRangeJSONCodec.decodableType, decoder = decoder)


	private fun decodeDouble(decoder: JSONDecoder<JSONCodingContext>): ClosedFloatingPointRange<Double> {
		var endInclusive = 0.0
		var endInclusiveProvided = false
		var start = 0.0
		var startProvided = false

		decoder.readFromMapByElementValue { key ->
			when (key) {
				Fields.endInclusive -> {
					endInclusive = readDouble()
					endInclusiveProvided = true
				}
				Fields.start -> {
					start = readDouble()
					startProvided = true
				}
				else -> skipValue()
			}
		}

		if (!startProvided) throw JSONException("missing '${Fields.start}'")
		if (!endInclusiveProvided) throw JSONException("missing '${Fields.endInclusive}'")

		return start .. endInclusive
	}


	private fun decodeFloat(decoder: JSONDecoder<JSONCodingContext>): ClosedFloatingPointRange<Float> {
		var endInclusive = 0.0f
		var endInclusiveProvided = false
		var start = 0.0f
		var startProvided = false

		decoder.readFromMapByElementValue { key ->
			when (key) {
				Fields.endInclusive -> {
					endInclusive = readFloat()
					endInclusiveProvided = true
				}
				Fields.start -> {
					start = readFloat()
					startProvided = true
				}
				else -> skipValue()
			}
		}

		if (!startProvided) throw JSONException("missing '${Fields.start}'")
		if (!endInclusiveProvided) throw JSONException("missing '${Fields.endInclusive}'")

		return start .. endInclusive
	}


	private fun decodeInt(decoder: JSONDecoder<JSONCodingContext>) =
		IntRangeJSONCodec.decode(valueType = IntRangeJSONCodec.decodableType, decoder = decoder)


	private fun decodeLong(decoder: JSONDecoder<JSONCodingContext>) =
		LongRangeJSONCodec.decode(valueType = LongRangeJSONCodec.decodableType, decoder = decoder)


	private fun decodeOther(elementType: JSONCodingType<Comparable<Any>>, decoder: JSONDecoder<JSONCodingContext>): ClosedRange<*> {
		var endInclusive: Comparable<Any>? = null
		var start: Comparable<Any>? = null

		decoder.readFromMapByElementValue { key ->
			when (key) {
				Fields.endInclusive -> endInclusive = readValueOfType(elementType)
				Fields.start -> start = readValueOfType(elementType)
				else -> skipValue()
			}
		}

		return (start ?: throw JSONException("missing '${Fields.start}'")) ..
			(endInclusive ?: throw JSONException("missing '${Fields.endInclusive}'"))
	}


	override fun encode(value: ClosedRange<*>, encoder: JSONEncoder<JSONCodingContext>) {
		encoder.writeIntoMap {
			writeMapElement(Fields.start, value = value.start)
			writeMapElement(Fields.endInclusive, value = value.endInclusive)
		}
	}


	private object Fields {

		const val endInclusive = "endInclusive"
		const val start = "start"
	}
}
