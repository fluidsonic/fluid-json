package json.representation

import io.fluidsonic.json.*


@Json(
	representation = Json.Representation.structured
)
class StructuredValueGeneric<Value>(val value: Value)
