package com.github.fluidsonic.fluid.json.annotationprocessor

import com.github.fluidsonic.fluid.meta.*
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.WildcardTypeName


internal fun MClassReference.forKotlinPoet(): TypeName {
	val typeNames = name.withoutPackage().kotlin.split('.')

	val c: ClassName = ClassName(
		packageName = name.packageName.kotlin,
		simpleName = typeNames.first(),
		simpleNames = *typeNames.drop(1).toTypedArray()
	)
		.copy(nullable = isNullable)

	if (this.arguments.isNotEmpty()) {
		return c.parameterizedBy(*arguments.map { it.forKotlinPoet() }.toTypedArray())
	}
	else
		return c
}

internal fun MTypeReference.forKotlinPoet() =
	when (this) {
		is MClassReference -> forKotlinPoet()
		else -> error("not yet supported") // FIXME
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
