package com.github.fluidsonic.fluid.json

import java.time.*


object PeriodJSONCodec : AbstractJSONCodec<Period, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<Period>) =
		readString().let { raw ->
			try {
				Period.parse(raw)!!
			}
			catch (e: DateTimeException) {
				invalidValueError("period in ISO-8601 format expected, got '$raw'")
			}
		}


	override fun JSONEncoder<JSONCodingContext>.encode(value: Period) =
		writeString(value.toString())
}
