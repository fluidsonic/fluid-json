package com.github.fluidsonic.fluid.json

import org.apiguardian.api.API


@API(status = API.Status.EXPERIMENTAL)
class JSONException : RuntimeException {

	@API(status = API.Status.EXPERIMENTAL)
	constructor()

	@API(status = API.Status.EXPERIMENTAL)
	constructor(message: String) : super(message)
}
