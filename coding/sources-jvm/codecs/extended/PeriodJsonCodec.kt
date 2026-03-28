package io.fluidsonic.json

import java.time.*


public object PeriodJsonCodec : AbstractJsonCodec<Period, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Period>): Period =
		readString().let { raw ->
			try {
				Period.parse(raw)!!
			}
			catch (e: DateTimeException) {
				invalidValueError("period in ISO-8601 format expected, got '$raw'")
			}
		}


	override fun JsonEncoder<JsonCodingContext>.encode(value: Period): Unit =
		writeString(value.toString())
}
