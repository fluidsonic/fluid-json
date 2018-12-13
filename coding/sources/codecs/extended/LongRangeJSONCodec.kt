package com.github.fluidsonic.fluid.json


object LongRangeJSONCodec : AbstractJSONCodec<LongRange, JSONCodingContext>() {

	override fun decode(valueType: JSONCodingType<in LongRange>, decoder: JSONDecoder<JSONCodingContext>): LongRange {
		var endInclusive = 0L
		var endInclusiveProvided = false
		var start = 0L
		var startProvided = false

		decoder.readFromMapByElementValue { key ->
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

		if (!startProvided) throw JSONException("missing '${Fields.start}'")
		if (!endInclusiveProvided) throw JSONException("missing '${Fields.endInclusive}'")

		return start .. endInclusive
	}


	override fun encode(value: LongRange, encoder: JSONEncoder<JSONCodingContext>) {
		encoder.writeIntoMap {
			writeMapElement(Fields.start, long = value.first)
			writeMapElement(Fields.endInclusive, long = value.last)
		}
	}


	private object Fields {

		const val endInclusive = "endInclusive"
		const val start = "start"
	}
}
