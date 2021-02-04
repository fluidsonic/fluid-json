package json.representation

import io.fluidsonic.json.*


@Json(
	representation = Json.Representation.structured
)
inline class Structured(val value: String)
