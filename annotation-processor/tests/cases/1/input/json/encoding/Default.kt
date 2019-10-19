package json.encoding

import io.fluidsonic.json.*
import java.io.Serializable


@Json
class Default(
	val value1: String
) : Serializable by "" {

	val value2 = ""

	@Json.Excluded
	val value3 = ""

	private val value4 = ""

	val value5
		get() = ""

	@Json.Property
	val value6
		get() = ""

	val Unit.value7
		get() = ""
}

val Default.value8
	get() = ""

@Json.Property
val Default.value9
	get() = ""
