package json.representation

import io.fluidsonic.json.*


@Json(
	representation = Json.Representation.automatic
)
inline class AutomaticSingleValue(val value: String)
