package property.serializedName

import com.github.fluidsonic.fluid.json.*


@JSON
class Default(
	@JSON.Property
	val value1: String
) {

	@JSON.Property
	val value2 = ""
}
