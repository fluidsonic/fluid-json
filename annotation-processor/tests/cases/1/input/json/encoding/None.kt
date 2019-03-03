package json.encoding

import com.github.fluidsonic.fluid.json.*
import java.io.Serializable


@JSON(
	encoding = JSON.Encoding.none
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
