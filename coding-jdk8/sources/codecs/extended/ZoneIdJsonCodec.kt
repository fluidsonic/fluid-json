package io.fluidsonic.json

import java.time.*


object ZoneIdJsonCodec : AbstractJsonCodec<ZoneId, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<ZoneId>): ZoneId =
		readString().let { raw ->
			try {
				ZoneId.of(raw)
			}
			catch (e: DateTimeException) {
				invalidValueError("IANA time zone name expected, got '$raw'")
			}
		}


	override fun JsonEncoder<JsonCodingContext>.encode(value: ZoneId) =
		writeString(value.id)
}
