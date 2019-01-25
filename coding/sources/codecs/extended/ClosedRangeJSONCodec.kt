package com.github.fluidsonic.fluid.json


object ClosedRangeJSONCodec : AbstractJSONCodec<ClosedRange<*>, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in ClosedRange<*>>): ClosedRange<*> {
		@Suppress("UNCHECKED_CAST")
		val elementType = valueType.arguments.single() as JSONCodingType<Comparable<Any>>

		return when (elementType.rawClass) {
			Char::class -> decodeCharRange()
			Comparable::class -> decodeComparableRange()
			Double::class -> decodeDoubleRange()
			Float::class -> decodeFloatRange()
			Int::class -> decodeIntRange()
			Long::class -> decodeLongRange()
			else -> decodeOtherRange(elementType = elementType)
		}
	}


	@Suppress("UNCHECKED_CAST")
	private fun JSONDecoder<JSONCodingContext>.decodeComparableRange(): ClosedRange<*> {
		var endInclusive: Comparable<Any>? = null
		var start: Comparable<Any>? = null

		readFromMapByElementValue { key ->
			when (key) {
				Fields.endInclusive -> endInclusive = readValue() as? Comparable<Any>
					?: invalidPropertyError(key, details = "expected a comparable value")

				Fields.start -> start = readValue() as? Comparable<Any>
					?: invalidPropertyError(key, details = "expected a comparable value")

				else -> skipValue()
			}
		}

		return (start ?: missingPropertyError(Fields.start)) .. (endInclusive ?: missingPropertyError(Fields.endInclusive))
	}


	private fun JSONDecoder<JSONCodingContext>.decodeCharRange() =
		CharRangeJSONCodec.run { decode(valueType = CharRangeJSONCodec.decodableType) }


	private fun JSONDecoder<JSONCodingContext>.decodeDoubleRange(): ClosedFloatingPointRange<Double> {
		var endInclusive = 0.0
		var endInclusiveProvided = false
		var start = 0.0
		var startProvided = false

		readFromMapByElementValue { key ->
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

		if (!startProvided) missingPropertyError(Fields.start)
		if (!endInclusiveProvided) missingPropertyError(Fields.endInclusive)

		return start .. endInclusive
	}


	private fun JSONDecoder<JSONCodingContext>.decodeFloatRange(): ClosedFloatingPointRange<Float> {
		var endInclusive = 0.0f
		var endInclusiveProvided = false
		var start = 0.0f
		var startProvided = false

		readFromMapByElementValue { key ->
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

		if (!startProvided) missingPropertyError(Fields.start)
		if (!endInclusiveProvided) missingPropertyError(Fields.endInclusive)

		return start .. endInclusive
	}


	private fun JSONDecoder<JSONCodingContext>.decodeIntRange() =
		IntRangeJSONCodec.run { decode(valueType = IntRangeJSONCodec.decodableType) }


	private fun JSONDecoder<JSONCodingContext>.decodeLongRange() =
		LongRangeJSONCodec.run { decode(valueType = LongRangeJSONCodec.decodableType) }


	private fun JSONDecoder<JSONCodingContext>.decodeOtherRange(elementType: JSONCodingType<Comparable<Any>>): ClosedRange<*> {
		var endInclusive: Comparable<Any>? = null
		var start: Comparable<Any>? = null

		readFromMapByElementValue { key ->
			when (key) {
				Fields.endInclusive -> endInclusive = readValueOfType(elementType)
				Fields.start -> start = readValueOfType(elementType)
				else -> skipValue()
			}
		}

		return (start ?: missingPropertyError(Fields.start)) .. (endInclusive ?: missingPropertyError(Fields.endInclusive))
	}


	override fun JSONEncoder<JSONCodingContext>.encode(value: ClosedRange<*>) {
		writeIntoMap {
			writeMapElement(Fields.start, value = value.start)
			writeMapElement(Fields.endInclusive, value = value.endInclusive)
		}
	}


	private object Fields {

		const val endInclusive = "endInclusive"
		const val start = "start"
	}
}
