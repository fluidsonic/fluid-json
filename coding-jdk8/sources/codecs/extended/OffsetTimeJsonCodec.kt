package io.fluidsonic.json

import java.time.*


object OffsetTimeJsonCodec : AbstractJsonCodec<OffsetTime, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<OffsetTime>) =
		readString().let { raw ->
			try {
				OffsetTime.parse(raw)!!
			}
			catch (e: DateTimeException) {
				invalidValueError("time and time zone in ISO-8601 format expected, got '$raw'")
			}
		}


	override fun JsonEncoder<JsonCodingContext>.encode(value: OffsetTime) =
		writeString(value.toString())
}
