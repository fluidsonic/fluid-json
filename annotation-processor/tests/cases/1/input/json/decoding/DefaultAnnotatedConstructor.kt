package json.decoding

import io.fluidsonic.json.*


@Json
class DefaultAnnotatedConstructor(
	val value: String
) {

	constructor(value: Int) : this(value.toString())

	@Json.Constructor
	constructor(value: Long) : this(value.toString())
}
