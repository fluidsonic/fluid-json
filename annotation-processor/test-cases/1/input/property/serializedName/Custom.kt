package property.serializedName

import io.fluidsonic.json.*


@Json
class Custom(
	@Json.Property(serializedName = "V A L U E 1")
	val value1: String
) {

	@Json.Property(serializedName = "V A L U E 2")
	val value2 = ""
}
