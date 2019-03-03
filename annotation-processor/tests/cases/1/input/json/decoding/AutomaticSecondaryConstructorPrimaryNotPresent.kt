package json.decoding

import com.github.fluidsonic.fluid.json.*


@JSON(
	decoding = JSON.Decoding.automatic
)
class AutomaticSecondaryConstructorPrimaryNotPresent {

	var value = ""


	constructor(value: Int) {
		this.value = value.toString()
	}
}
