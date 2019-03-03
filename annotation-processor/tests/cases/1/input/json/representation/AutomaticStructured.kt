package json.representation

import com.github.fluidsonic.fluid.json.*


@JSON(
	representation = JSON.Representation.automatic
)
class AutomaticStructured(val value: String)
