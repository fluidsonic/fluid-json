package io.fluidsonic.json.dynamic

import io.fluidsonic.json.*


internal interface CodingImplementationsJava {

	fun extendedCodecProviders(): List<JsonCodecProvider<JsonCodingContext>> =
		emptyList()
}


internal val codingImplementationsJava: CodingImplementationsJava by lazy {
	try {
		return@lazy Class.forName("io.fluidsonic.json.dynamic.CodingImplementationsJava8").getDeclaredConstructor().newInstance()
			as CodingImplementationsJava
	}
	catch (e: Exception) {
		// non-existent or severely broken
	}

	CodingImplementationsJava7()
}
