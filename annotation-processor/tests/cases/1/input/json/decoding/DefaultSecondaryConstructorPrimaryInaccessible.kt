package json.decoding

import com.github.fluidsonic.fluid.json.*


@JSON
class DefaultSecondaryConstructorPrimaryInaccessible private constructor(
	val value: String
) {

	constructor(value: Int) : this(value.toString())
}
