package json.representation

import io.fluidsonic.json.*


@Json(
	representation = Json.Representation.singleValue
)
class SingleValueNullable(val value: String?)
