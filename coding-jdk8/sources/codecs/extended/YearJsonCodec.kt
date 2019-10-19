package io.fluidsonic.json

import java.time.*


object YearJsonCodec : AbstractJsonCodec<Year, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Year>) =
		readInt().let { raw ->
			try {
				Year.of(raw)!!
			}
			catch (e: DateTimeException) {
				invalidValueError("year in ISO-8601 format expected, got '$raw'")
			}
		}


	override fun JsonEncoder<JsonCodingContext>.encode(value: Year) =
		writeInt(value.value)
}
