package json.codecVisibility

import io.fluidsonic.json.*


@Json(
	codecVisibility = Json.CodecVisibility.publicRequired
)
class Public(val value: String)
