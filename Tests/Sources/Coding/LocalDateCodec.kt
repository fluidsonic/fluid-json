package tests

import com.github.fluidsonic.fluid.json.JSONCodec
import com.github.fluidsonic.fluid.json.JSONCoderContext
import com.github.fluidsonic.fluid.json.JSONDecoder
import com.github.fluidsonic.fluid.json.JSONEncoder
import java.time.LocalDate


internal object LocalDateCodec : JSONCodec<LocalDate, JSONCoderContext> {

	override fun decode(decoder: JSONDecoder<out JSONCoderContext>) =
		LocalDate.parse(decoder.readString())!!


	override fun encode(value: LocalDate, encoder: JSONEncoder<out JSONCoderContext>) {
		encoder.writeString(value.toString())
	}


	override val valueClass = LocalDate::class.java
}
