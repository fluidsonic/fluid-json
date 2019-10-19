package json.encoding

import io.fluidsonic.json.*
import java.io.Serializable


@Json(
	encoding = Json.Encoding.none
)
class None(
	val value1: String
) : Serializable by "" {

	val value2 = ""

	private val value3 = ""

	val value4
		get() = ""

	val Unit.value5
		get() = ""
}

val None.value6
	get() = ""
