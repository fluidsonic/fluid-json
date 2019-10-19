package json.codecVisibility

import io.fluidsonic.json.*


internal class AutomaticInternalForContainedClass {

	@Json(
		codecVisibility = Json.CodecVisibility.automatic
	)
	class ContainedClass(val value: String)
}
