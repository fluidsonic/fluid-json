package json.encoding

import com.github.fluidsonic.fluid.json.*
import java.io.Serializable


@JSON(
	encoding = JSON.Encoding.annotatedProperties
)
class AnnotatedProperties(
	val value1: String
) : Serializable by "" {

	val value2 = ""

	@JSON.Property
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

val AnnotatedProperties.value8
	get() = ""

@JSON.Property
val AnnotatedProperties.value9
	get() = ""
