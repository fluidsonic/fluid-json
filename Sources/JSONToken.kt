package com.github.fluidsonic.fluid.json

import org.apiguardian.api.API


@API(status = API.Status.EXPERIMENTAL)
internal enum class JSONToken {

	booleanValue,
	arrayEnd,
	arrayStart,
	nullValue,
	numberValue,
	objectEnd,
	objectStart,
	stringValue
}
