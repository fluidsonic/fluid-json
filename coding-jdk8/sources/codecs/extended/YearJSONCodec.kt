package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.Year


object YearJSONCodec : AbstractJSONCodec<Year, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in Year>) =
		readInt().let { raw ->
			try {
				Year.of(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Invalid Year value: $raw")
			}
		}


	override fun JSONEncoder<JSONCodingContext>.encode(value: Year) =
		writeInt(value.value)
}
