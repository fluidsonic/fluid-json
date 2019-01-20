package tests.coding

import com.github.fluidsonic.fluid.json.AbstractJSONCodec
import com.github.fluidsonic.fluid.json.JSONCodingContext
import com.github.fluidsonic.fluid.json.JSONCodingType
import com.github.fluidsonic.fluid.json.JSONDecoder
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.JSONException


internal object YearMonthDayCodec : AbstractJSONCodec<YearMonthDay, JSONCodingContext>() {

	override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in YearMonthDay>): YearMonthDay {
		val stringValue = readString()
		return YearMonthDay.parse(stringValue) ?: throw JSONException("Cannot decode YearMonthDay '$stringValue'")
	}


	override fun JSONEncoder<JSONCodingContext>.encode(value: YearMonthDay) {
		writeString(value.toString())
	}
}
