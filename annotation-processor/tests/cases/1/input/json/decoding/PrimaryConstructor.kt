package json.decoding

import io.fluidsonic.json.*


@Json(
	decoding = Json.Decoding.primaryConstructor
)
class PrimaryConstructor(
	val value: String
) {

	constructor(value: Int) : this(value.toString())
}
