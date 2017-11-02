package tests

import com.github.fluidsonic.fluid.json.*
import java.time.LocalDate


internal object LocalDateCodec : AbstractJSONCodec<LocalDate, JSONCoderContext>() {

	override fun decode(valueType: JSONCodableType<in LocalDate>, decoder: JSONDecoder<JSONCoderContext>): LocalDate {
		val stringValue = decoder.readString()
		return LocalDate.parse(stringValue) ?: throw JSONException("Cannot decode LocalDate '$stringValue'")
	}


	override fun encode(value: LocalDate, encoder: JSONEncoder<JSONCoderContext>) {
		encoder.writeString(value.toString())
	}
}
