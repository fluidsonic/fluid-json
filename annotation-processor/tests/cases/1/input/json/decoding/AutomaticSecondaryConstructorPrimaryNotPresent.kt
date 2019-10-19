package json.decoding

import io.fluidsonic.json.*


@Json(
	decoding = Json.Decoding.automatic
)
class AutomaticSecondaryConstructorPrimaryNotPresent {

	var value = ""


	constructor(value: Int) {
		this.value = value.toString()
	}
}
