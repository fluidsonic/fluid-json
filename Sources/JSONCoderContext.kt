package com.github.fluidsonic.fluid.json


interface JSONCoderContext {

	companion object {

		val empty = object : JSONCoderContext {}
	}
}
