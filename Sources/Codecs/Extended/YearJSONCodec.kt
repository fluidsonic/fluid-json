package com.github.fluidsonic.fluid.json

import java.time.DateTimeException
import java.time.Year


object YearJSONCodec : AbstractJSONCodec<Year, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in Year>, decoder: JSONDecoder<JSONCoderContext>) =
		decoder.readInt().let { raw ->
			try {
				Year.of(raw)!!
			}
			catch (e: DateTimeException) {
				throw JSONException("Invalid Year value: $raw")
			}
		}


	override fun encode(value: Year, encoder: JSONEncoder<JSONCoderContext>) =
		encoder.writeInt(value.value)
}
