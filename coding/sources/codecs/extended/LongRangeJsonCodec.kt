package io.fluidsonic.json


object LongRangeJsonCodec : AbstractJsonCodec<LongRange, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<LongRange>): LongRange {
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


	override fun JsonEncoder<JsonCodingContext>.encode(value: LongRange) {
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
