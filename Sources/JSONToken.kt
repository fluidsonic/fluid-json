package com.github.fluidsonic.fluid.json


enum class JSONToken {

	booleanValue,
	listEnd,
	listStart,
	nullValue,
	mapEnd,
	mapKey,
	mapStart,
	numberValue,
	stringValue;


	companion object
}
