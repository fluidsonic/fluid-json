package json.decoding

import com.github.fluidsonic.fluid.json.*


@JSON(
	decoding = JSON.Decoding.automatic
)
class AutomaticSecondaryConstructorPrimaryInaccessible private constructor(
	val value: String
) {

	constructor(value: Int) : this(value.toString())
}
