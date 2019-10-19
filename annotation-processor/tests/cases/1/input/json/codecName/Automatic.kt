package json.codecName

import io.fluidsonic.json.*


@Json(
	codecName = Json.automatic
)
class Automatic(val value: String)
