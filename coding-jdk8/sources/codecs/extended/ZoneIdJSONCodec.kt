package com.github.fluidsonic.fluid.json

import java.time.*


object ZoneIdJSONCodec : AbstractJSONCodec<ZoneId, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<ZoneId>) =
		readString().let { raw ->
			try {
				ZoneId.of(raw)!!
			}
			catch (e: DateTimeException) {
				invalidValueError("IANA time zone name expected, got '$raw'")
			}
		}


	override fun JSONEncoder<JSONCodingContext>.encode(value: ZoneId) =
		writeString(value.id)
}
