package json.decoding

import com.github.fluidsonic.fluid.json.*


@JSON(
	decoding = JSON.Decoding.automatic
)
class AutomaticSecondaryConstructorPrimaryExcluded @JSON.Excluded constructor(
	val value: String
) {

	constructor(value: Int) : this(value.toString())
}
