package json.encoding

import com.github.fluidsonic.fluid.json.*
import java.io.Serializable


@JSON
class Default(
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

val Default.value8
	get() = ""

@JSON.Property
val Default.value9
	get() = ""
