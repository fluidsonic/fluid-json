package io.fluidsonic.json.annotationprocessor

import com.squareup.kotlinpoet.*
import io.fluidsonic.meta.*


internal fun MQualifiedTypeName.forKotlinPoet(nullable: Boolean = false) =
	ClassName(packageName.kotlin, withoutPackage().kotlin)
		.copy(nullable = nullable) as ClassName
