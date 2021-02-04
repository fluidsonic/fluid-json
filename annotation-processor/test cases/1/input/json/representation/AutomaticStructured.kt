package json.representation

import io.fluidsonic.json.*


@Json(
	representation = Json.Representation.automatic
)
class AutomaticStructured(val value: String)
