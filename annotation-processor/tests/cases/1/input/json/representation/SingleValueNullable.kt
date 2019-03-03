package json.representation

import com.github.fluidsonic.fluid.json.*


@JSON(
	representation = JSON.Representation.singleValue
)
class SingleValueNullable(val value: String?)
