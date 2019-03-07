package com.github.fluidsonic.fluid.json


object LongRangeJSONCodec : AbstractJSONCodec<LongRange, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<LongRange>): LongRange {
		var endInclusive = 0L
		var endInclusiveProvided = false
		var start = 0L
		var startProvided = false

		readFromMapByElementValue { key ->
			when (key) {
				Fields.endInclusive -> {
					endInclusive = readLong()
					endInclusiveProvided = true
				}
				Fields.start -> {
					start = readLong()
					startProvided = true
				}
				else -> skipValue()
			}
		}

		if (!startProvided) missingPropertyError(Fields.start)
		if (!endInclusiveProvided) missingPropertyError(Fields.endInclusive)

		return start .. endInclusive
	}


	override fun JSONEncoder<JSONCodingContext>.encode(value: LongRange) {
		writeIntoMap {
			writeMapElement(Fields.start, long = value.first)
			writeMapElement(Fields.endInclusive, long = value.last)
		}
	}


	private object Fields {

		const val endInclusive = "endInclusive"
		const val start = "start"
	}
}
