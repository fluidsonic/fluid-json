package com.github.fluidsonic.fluid.json.annotationprocessor

import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName


internal object KotlinpoetTypeNames {

	val any = ANY
	val boolean = ClassName("kotlin", "Boolean")
	val byte = ClassName("kotlin", "Byte")
	val char = ClassName("kotlin", "Char")
	val double = ClassName("kotlin", "Double")
	val float = ClassName("kotlin", "Float")
	val int = ClassName("kotlin", "Int")
	val long = ClassName("kotlin", "Long")
	val short = ClassName("kotlin", "Short")
	val string = ClassName("kotlin", "String")

	val nullableAny = any.copy(nullable = true)

	val basic = setOf(
		boolean,
		byte,
		char,
		double,
		float,
		int,
		long,
		short
	)
}


internal val TypeName.isPrimitive
	get() = KotlinpoetTypeNames.basic.contains(this)
