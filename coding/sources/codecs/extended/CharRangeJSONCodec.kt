package com.github.fluidsonic.fluid.json


object CharRangeJSONCodec : AbstractJSONCodec<CharRange, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in CharRange>): CharRange {
		var endInclusive = 0.toChar()
		var endInclusiveProvided = false
		var start = 0.toChar()
		var startProvided = false

		readFromMapByElementValue { key ->
			when (key) {
				Fields.endInclusive -> {
					endInclusive = readChar()
					endInclusiveProvided = true
				}
				Fields.start -> {
					start = readChar()
					startProvided = true
				}
				else -> skipValue()
			}
		}

		if (!startProvided) throw JSONException("missing '${Fields.start}'")
		if (!endInclusiveProvided) throw JSONException("missing '${Fields.endInclusive}'")

		return start .. endInclusive
	}


	override fun JSONEncoder<JSONCodingContext>.encode(value: CharRange) {
		writeIntoMap {
			writeMapElement(Fields.start, char = value.first)
			writeMapElement(Fields.endInclusive, char = value.last)
		}
	}


	private object Fields {

		const val endInclusive = "endInclusive"
		const val start = "start"
	}
}
