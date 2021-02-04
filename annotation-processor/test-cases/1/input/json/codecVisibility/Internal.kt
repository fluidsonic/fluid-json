package json.codecVisibility

import io.fluidsonic.json.*


@Json(
	codecVisibility = Json.CodecVisibility.internal
)
class Internal(val value: String)
