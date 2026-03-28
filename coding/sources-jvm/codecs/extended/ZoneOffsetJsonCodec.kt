package io.fluidsonic.json

import java.time.*


public object ZoneOffsetJsonCodec : AbstractJsonCodec<ZoneOffset, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<ZoneOffset>): ZoneOffset =
		readString().let { raw ->
			try {
				ZoneOffset.of(raw)!!
			}
			catch (e: DateTimeException) {
				invalidValueError("time offset in ISO-8601 format expected, got '$raw'")
			}
		}


	override fun JsonEncoder<JsonCodingContext>.encode(value: ZoneOffset): Unit =
		writeString(value.id)
}
