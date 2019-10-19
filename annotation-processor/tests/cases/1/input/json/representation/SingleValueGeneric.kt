package json.representation

import io.fluidsonic.json.*


@Json(
	representation = Json.Representation.singleValue
)
class SingleValueGeneric<Value : SingleValueGeneric.Bound?>(val value: Value) {

	interface Bound
}
