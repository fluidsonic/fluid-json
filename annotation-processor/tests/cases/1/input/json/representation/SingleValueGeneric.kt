package json.representation

import com.github.fluidsonic.fluid.json.*


@JSON(
	representation = JSON.Representation.singleValue
)
class SingleValueGeneric<Value : SingleValueGeneric.Bound?>(val value: Value) {

	interface Bound
}
