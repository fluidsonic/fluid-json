package tests.coding

import com.github.fluidsonic.fluid.json.*


internal object YearMonthDayCodec : AbstractJSONCodec<YearMonthDay, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<YearMonthDay>) =
		readString().let { raw ->
			YearMonthDay.parse(raw) ?: invalidValueError("expected date in format YYYY-MM-DD, got '$raw'")
		}


	override fun JSONEncoder<JSONCodingContext>.encode(value: YearMonthDay) {
		writeString(value.toString())
	}
}
