package json.codecName

import io.fluidsonic.json.*


@Json(
	codecName = "CustomizedJsonCodec"
)
class Custom(val value: String)
