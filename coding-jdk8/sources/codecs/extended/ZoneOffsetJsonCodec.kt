package io.fluidsonic.json

import java.time.*


object ZoneOffsetJsonCodec : AbstractJsonCodec<ZoneOffset, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<ZoneOffset>) =
		readString().let { raw ->
			try {
				ZoneOffset.of(raw)!!
			}
			catch (e: DateTimeException) {
				invalidValueError("time offset in ISO-8601 format expected, got '$raw'")
			}
		}


	override fun JsonEncoder<JsonCodingContext>.encode(value: ZoneOffset) =
		writeString(value.id)
}
