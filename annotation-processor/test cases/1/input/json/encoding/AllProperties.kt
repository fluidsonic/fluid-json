package json.encoding

import io.fluidsonic.json.*
import java.io.Serializable


@Json(
	encoding = Json.Encoding.allProperties
)
class AllProperties(
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

val AllProperties.value8
	get() = ""

@Json.Property
val AllProperties.value9
	get() = ""
