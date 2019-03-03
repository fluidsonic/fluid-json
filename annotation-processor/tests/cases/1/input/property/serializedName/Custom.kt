package property.serializedName

import com.github.fluidsonic.fluid.json.*


@JSON
class Custom(
	@JSON.Property(serializedName = "V A L U E 1")
	val value1: String
) {

	@JSON.Property(serializedName = "V A L U E 2")
	val value2 = ""
}
