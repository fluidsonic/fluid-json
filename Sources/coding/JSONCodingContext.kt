package com.github.fluidsonic.fluid.json


interface JSONCodingContext {

	companion object {

		val empty = object : JSONCodingContext {}
	}
}
