package property.serializedName

import io.fluidsonic.json.*


@Json
class Default(
	@Json.Property
	val value1: String
) {

	@Json.Property
	val value2 = ""
}
