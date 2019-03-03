package json.codecName

import com.github.fluidsonic.fluid.json.*


@JSON(
	codecName = "CustomizedJSONCodec"
)
class Custom(val value: String)
