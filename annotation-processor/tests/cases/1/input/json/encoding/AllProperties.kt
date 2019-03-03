package json.encoding

import com.github.fluidsonic.fluid.json.*
import java.io.Serializable


@JSON(
	encoding = JSON.Encoding.allProperties
)
class AllProperties(
	val value1: String
) : Serializable by "" {

	val value2 = ""

	@JSON.Excluded
	val value3 = ""

	private val value4 = ""

	val value5
		get() = ""

	@JSON.Property
	val value6
		get() = ""

	val Unit.value7
		get() = ""
}

val AllProperties.value8
	get() = ""

@JSON.Property
val AllProperties.value9
	get() = ""
