package json.decoding

import io.fluidsonic.json.*


@Json(
	decoding = Json.Decoding.none
)
class None(
	val value: String
) {

	constructor(value: Int) : this(value.toString())
}
