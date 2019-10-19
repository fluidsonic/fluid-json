package json.decoding

import io.fluidsonic.json.*


@Json(
	decoding = Json.Decoding.automatic
)
class AutomaticSecondaryConstructorPrimaryInaccessible private constructor(
	val value: String
) {

	constructor(value: Int) : this(value.toString())
}
