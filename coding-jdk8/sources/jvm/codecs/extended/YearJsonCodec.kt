package io.fluidsonic.json

import java.time.*


public object YearJsonCodec : AbstractJsonCodec<Year, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Year>): Year =
		readInt().let { raw ->
			try {
				Year.of(raw)!!
			}
			catch (e: DateTimeException) {
				invalidValueError("year in ISO-8601 format expected, got '$raw'")
			}
		}


	override fun JsonEncoder<JsonCodingContext>.encode(value: Year): Unit =
		writeInt(value.value)
}
