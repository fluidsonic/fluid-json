package json.decoding

import io.fluidsonic.json.*


@Json(
	decoding = Json.Decoding.automatic
)
class AutomaticPrimaryConstructor(
	val value: String
)
