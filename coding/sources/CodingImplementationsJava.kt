package com.github.fluidsonic.fluid.json.dynamic

import com.github.fluidsonic.fluid.json.*


internal interface CodingImplementationsJava {

	fun extendedCodecProviders(): List<JSONCodecProvider<JSONCodingContext>> =
		emptyList()
}


internal val codingImplementationsJava: CodingImplementationsJava by lazy {
	try {
		return@lazy Class.forName("com.github.fluidsonic.fluid.json.dynamic.CodingImplementationsJava8").getDeclaredConstructor().newInstance()
			as CodingImplementationsJava
	}
	catch (e: Exception) {
		// non-existent or severely broken
	}

	CodingImplementationsJava7()
}
