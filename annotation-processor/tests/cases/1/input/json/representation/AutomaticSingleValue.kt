package json.representation

import com.github.fluidsonic.fluid.json.*


@JSON(
	representation = JSON.Representation.automatic
)
inline class AutomaticSingleValue(val value: String)
