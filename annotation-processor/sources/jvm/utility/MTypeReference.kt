package io.fluidsonic.json.annotationprocessor

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import io.fluidsonic.meta.*


internal fun MTypeReference.Class.forKotlinPoet(typeParameters: List<MTypeParameter>): TypeName {
	val typeNames = name.withoutPackage().kotlin.split('.')

	return ClassName(
		packageName = name.packageName.kotlin,
		simpleNames = typeNames
	)
		.let { className ->
			if (this.arguments.isNotEmpty()) {
				className.parameterizedBy(*arguments.map { it.forKotlinPoet(typeParameters = typeParameters) }.toTypedArray())
			}
			else
				className
		}
		.copy(nullable = isNullable)
}


internal fun MTypeReference.TypeParameter.forKotlinPoet(typeParameters: List<MTypeParameter>): TypeName =
	typeParameters.first { it.id == id }.let { typeParameter ->
		TypeVariableName(
			name = typeParameter.name.kotlin,
			bounds = typeParameter.upperBounds
				.map { it.forKotlinPoet(typeParameters = typeParameters) }
				.ifEmpty { listOf(KotlinpoetTypeNames.nullableAny) }
				.toTypedArray(),
			variance = typeParameter.variance.kModifier
		).copy(nullable = isNullable || typeParameter.upperBounds.all { it.isNullable })
	}


internal fun MTypeReference.forKotlinPoet(typeParameters: List<MTypeParameter>): TypeName =
	when (this) {
		is MTypeReference.Class -> forKotlinPoet(typeParameters = typeParameters)
		is MTypeReference.TypeParameter -> forKotlinPoet(typeParameters = typeParameters)
		else -> error("not supported")
	}


internal fun MTypeArgument.forKotlinPoet(typeParameters: List<MTypeParameter>): TypeName =
	when (this) {
		is MTypeArgument.StarProjection -> STAR
		is MTypeArgument.Type -> {
			when (variance) {
				MVariance.IN -> WildcardTypeName.consumerOf(forKotlinPoet(typeParameters = typeParameters))
				MVariance.OUT -> WildcardTypeName.producerOf(forKotlinPoet(typeParameters = typeParameters))
				MVariance.INVARIANT -> type.forKotlinPoet(typeParameters = typeParameters)
			}
		}
	}


private val MVariance.kModifier
	get() = when (this) {
		MVariance.INVARIANT -> null
		MVariance.IN -> KModifier.IN
		MVariance.OUT -> KModifier.OUT
	}
