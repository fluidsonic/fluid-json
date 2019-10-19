package json.decoding

import io.fluidsonic.json.*


@Json(
	decoding = Json.Decoding.automatic
)
class AutomaticSecondaryConstructorPrimaryExcluded @Json.Excluded constructor(
	val value: String
) {

	constructor(value: Int) : this(value.toString())
}
