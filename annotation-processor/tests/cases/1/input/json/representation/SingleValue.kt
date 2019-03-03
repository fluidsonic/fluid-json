package json.representation

import com.github.fluidsonic.fluid.json.*


@JSON(
	representation = JSON.Representation.singleValue
)
class SingleValue(val value: String)
