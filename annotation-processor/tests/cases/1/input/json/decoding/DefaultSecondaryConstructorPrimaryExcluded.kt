package json.decoding

import io.fluidsonic.json.*


@Json
class DefaultSecondaryConstructorPrimaryExcluded @Json.Excluded constructor(
	val value: String
) {

	constructor(value: Int) : this(value.toString())
}
