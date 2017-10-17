package com.github.fluidsonic.fluid.json

import java.io.Reader
import java.io.StringReader


internal object JSON {

	fun parser(): JSONParser =
		SimpleParser


	fun reader(source: Reader): JSONReader =
		TextInputReader(TextInput(source))


	fun reader(source: String) =
		reader(StringReader(source))
}
