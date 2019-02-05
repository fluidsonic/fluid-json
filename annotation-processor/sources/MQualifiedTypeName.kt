package com.github.fluidsonic.fluid.json.annotationprocessor

import com.github.fluidsonic.fluid.meta.*
import com.squareup.kotlinpoet.ClassName


internal fun MQualifiedTypeName.forKotlinPoet(nullable: Boolean = false) =
	ClassName(packageName.kotlin, withoutPackage().kotlin)
		.copy(nullable = nullable)
