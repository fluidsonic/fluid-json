package tests

import com.github.fluidsonic.fluid.json.JSONCodec
import com.github.fluidsonic.fluid.json.JSONCoderContext
import com.github.fluidsonic.fluid.json.JSONDecoder
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.JSONException
import java.time.LocalDate


internal object LocalDateCodec : JSONCodec<LocalDate, JSONCoderContext> {

	override fun decode(decoder: JSONDecoder<out JSONCoderContext>): LocalDate {
		val stringValue = decoder.readString()
		return LocalDate.parse(stringValue) ?: throw JSONException("Cannot decode LocalDate '$stringValue'")
	}


	override fun encode(value: LocalDate, encoder: JSONEncoder<out JSONCoderContext>) {
		encoder.writeString(value.toString())
	}


	override val decodableClass = LocalDate::class.java
}
