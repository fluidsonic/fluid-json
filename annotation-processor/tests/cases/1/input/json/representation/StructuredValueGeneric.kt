package json.representation

import com.github.fluidsonic.fluid.json.*


@JSON(
	representation = JSON.Representation.structured
)
class StructuredValueGeneric<Value>(val value: Value)
