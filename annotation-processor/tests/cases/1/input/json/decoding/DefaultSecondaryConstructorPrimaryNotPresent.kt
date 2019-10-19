package json.decoding

import io.fluidsonic.json.*


@Json
class DefaultSecondaryConstructorPrimaryNotPresent {

	var value = ""


	constructor(value: Int) {
		this.value = value.toString()
	}
}
