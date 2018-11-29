package tests

import com.github.fluidsonic.fluid.json.*
import java.time.LocalDate


internal object LocalDateCodec : AbstractJSONCodec<LocalDate, JSONCodingContext>() {

	override fun decode(valueType: JSONCodingType<in LocalDate>, decoder: JSONDecoder<JSONCodingContext>): LocalDate {
		val stringValue = decoder.readString()
		return LocalDate.parse(stringValue) ?: throw JSONException("Cannot decode LocalDate '$stringValue'")
	}


	override fun encode(value: LocalDate, encoder: JSONEncoder<JSONCodingContext>) {
		encoder.writeString(value.toString())
	}
}
