package com.github.fluidsonic.fluid.json


object IntRangeJSONCodec : AbstractJSONCodec<IntRange, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in IntRange>): IntRange {
		var endInclusive = 0
		var endInclusiveProvided = false
		var start = 0
		var startProvided = false

		readFromMapByElementValue { key ->
			when (key) {
				Fields.endInclusive -> {
					endInclusive = readInt()
					endInclusiveProvided = true
				}
				Fields.start -> {
					start = readInt()
					startProvided = true
				}
				else -> skipValue()
			}
		}

		if (!startProvided) throw JSONException("missing '${Fields.start}'")
		if (!endInclusiveProvided) throw JSONException("missing '${Fields.endInclusive}'")

		return start .. endInclusive
	}


	override fun JSONEncoder<JSONCodingContext>.encode(value: IntRange) {
		writeIntoMap {
			writeMapElement(Fields.start, int = value.first)
			writeMapElement(Fields.endInclusive, int = value.last)
		}
	}


	private object Fields {

		const val endInclusive = "endInclusive"
		const val start = "start"
	}
}
