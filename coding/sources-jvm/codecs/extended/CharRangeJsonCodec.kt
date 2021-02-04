package io.fluidsonic.json


public object CharRangeJsonCodec : AbstractJsonCodec<CharRange, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<CharRange>): CharRange {
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

		if (!startProvided) missingPropertyError(Fields.start)
		if (!endInclusiveProvided) missingPropertyError(Fields.endInclusive)

		return start .. endInclusive
	}


	override fun JsonEncoder<JsonCodingContext>.encode(value: CharRange) {
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
