package property.defaultValue

import com.github.fluidsonic.fluid.json.*


@JSON
class Automatic(
	val value1: String,
	val value2: String = "",
	val value3: String?,
	val value4: String? = "",
	val value5: Double,
	val value6: Double = 1.2,
	val value7: Double?,
	val value8: Double? = 1.2
)
