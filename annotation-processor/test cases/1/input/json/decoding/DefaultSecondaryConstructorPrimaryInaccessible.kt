package json.decoding

import io.fluidsonic.json.*


@Json
class DefaultSecondaryConstructorPrimaryInaccessible private constructor(
	val value: String
) {

	constructor(value: Int) : this(value.toString())
}
