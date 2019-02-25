package com.github.fluidsonic.fluid.json.annotationprocessor

import com.github.fluidsonic.fluid.meta.*
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.WildcardTypeName


internal fun MTypeReference.Class.forKotlinPoet(): TypeName {
	val typeNames = name.withoutPackage().kotlin.split('.')

	return ClassName(
		packageName = name.packageName.kotlin,
		simpleName = typeNames.first(),
		simpleNames = *typeNames.drop(1).toTypedArray()
	)
		.let { className ->
			if (this.arguments.isNotEmpty()) {
				className.parameterizedBy(*arguments.map { it.forKotlinPoet() }.toTypedArray())
			}
			else
				className
		}
		.copy(nullable = isNullable)
}


internal fun MTypeReference.forKotlinPoet() =
	when (this) {
		is MTypeReference.Class -> forKotlinPoet()
		else -> error("not supported")
	}


internal fun MTypeArgument.forKotlinPoet(): TypeName =
	when (this) {
		is MTypeArgument.StarProjection -> STAR
		is MTypeArgument.Type -> {
			when (variance) {
				MVariance.IN -> WildcardTypeName.consumerOf(forKotlinPoet())
				MVariance.OUT -> WildcardTypeName.producerOf(forKotlinPoet())
				MVariance.INVARIANT -> type.forKotlinPoet()
			}
		}
	}
