package com.github.fluidsonic.fluid.json.annotationprocessor

import com.github.fluidsonic.fluid.meta.*
import com.squareup.kotlinpoet.*


internal fun MQualifiedTypeName.forKotlinPoet(nullable: Boolean = false) =
	ClassName(packageName.kotlin, withoutPackage().kotlin)
		.copy(nullable = nullable) as ClassName
