package property.serializedName

import com.github.fluidsonic.fluid.json.*


@JSON
class Automatic(
	@JSON.Property(serializedName = JSON.automatic)
	val value1: String
) {

	@JSON.Property(serializedName = JSON.automatic)
	val value2 = ""
}
