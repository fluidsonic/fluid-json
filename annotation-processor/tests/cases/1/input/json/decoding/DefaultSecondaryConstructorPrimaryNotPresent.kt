package json.decoding

import com.github.fluidsonic.fluid.json.*


@JSON
class DefaultSecondaryConstructorPrimaryNotPresent {

	var value = ""


	constructor(value: Int) {
		this.value = value.toString()
	}
}
