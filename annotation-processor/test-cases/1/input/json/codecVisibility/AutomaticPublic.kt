package json.codecVisibility

import io.fluidsonic.json.*


@Json(
	codecVisibility = Json.CodecVisibility.automatic
)
class AutomaticPublic(val value: String)
