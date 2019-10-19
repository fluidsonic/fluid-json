package property.serializedName

import io.fluidsonic.json.*


@Json
class Automatic(
	@Json.Property(serializedName = Json.automatic)
	val value1: String
) {

	@Json.Property(serializedName = Json.automatic)
	val value2 = ""
}
