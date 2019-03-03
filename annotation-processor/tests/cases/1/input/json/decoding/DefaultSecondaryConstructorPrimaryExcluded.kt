package json.decoding

import com.github.fluidsonic.fluid.json.*


@JSON
class DefaultSecondaryConstructorPrimaryExcluded @JSON.Excluded constructor(
	val value: String
) {

	constructor(value: Int) : this(value.toString())
}
