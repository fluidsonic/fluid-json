package json.representation

import com.github.fluidsonic.fluid.json.*


@JSON(
	representation = JSON.Representation.structured
)
inline class Structured(val value: String)
