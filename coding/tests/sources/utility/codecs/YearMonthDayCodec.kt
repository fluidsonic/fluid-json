package tests.coding

import io.fluidsonic.json.*


internal object YearMonthDayCodec : AbstractJsonCodec<YearMonthDay, JsonCodingContext>() {

	override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<YearMonthDay>) =
		readString().let { raw ->
			YearMonthDay.parse(raw) ?: invalidValueError("expected date in format YYYY-MM-DD, got '$raw'")
		}


	override fun JsonEncoder<JsonCodingContext>.encode(value: YearMonthDay) {
		writeString(value.toString())
	}
}
