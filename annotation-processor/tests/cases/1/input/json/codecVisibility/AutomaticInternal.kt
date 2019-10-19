package json.codecVisibility

import io.fluidsonic.json.*


@Json(
	codecVisibility = Json.CodecVisibility.automatic
)
internal class AutomaticInternal(val value: String)
