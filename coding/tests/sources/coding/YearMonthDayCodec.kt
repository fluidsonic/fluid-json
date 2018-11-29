package tests.coding

import com.github.fluidsonic.fluid.json.*


internal object YearMonthDayCodec : AbstractJSONCodec<YearMonthDay, JSONCodingContext>() {

	override fun decode(valueType: JSONCodingType<in YearMonthDay>, decoder: JSONDecoder<JSONCodingContext>): YearMonthDay {
		val stringValue = decoder.readString()
		return YearMonthDay.parse(stringValue) ?: throw JSONException("Cannot decode YearMonthDay '$stringValue'")
	}


	override fun encode(value: YearMonthDay, encoder: JSONEncoder<JSONCodingContext>) {
		encoder.writeString(value.toString())
	}
}
