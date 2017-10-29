package com.github.fluidsonic.fluid.json

import java.lang.reflect.GenericArrayType
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.WildcardType
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.KVariance


inline fun <reified Type : Any> jsonCodableType() =
	JSONCodableType.of<Type>(object : JSONCodableTypeReference<Type>() {}::class.java.genericSuperclass as ParameterizedType)


@Suppress("unused")
class JSONCodableType<Type : Any> private constructor(
	val rawClass: KClass<Type>,
	val arguments: List<JSONCodableType<*>>,
	upperBound: KClass<*>?,
	variance: KVariance
) {

	private val hashCode =
		rawClass.hashCode() xor arguments.hashCode()


	internal val isMostOpenUpperBound = (rawClass == upperBound && (variance == KVariance.OUT || variance == KVariance.INVARIANT))


	override fun equals(other: Any?): Boolean {
		if (this === other) {
			return true
		}

		if (other !is JSONCodableType<*>) {
			return false
		}

		return rawClass == other.rawClass && arguments == other.arguments
	}


	override fun hashCode() =
		hashCode


	override fun toString(): String =
		buildString {
			append(rawClass.qualifiedName)

			if (arguments.isNotEmpty()) {
				append("<")
				arguments.joinTo(this, separator = ", ") {
					if (it.isMostOpenUpperBound) "*" else it.toString()
				}
				append(">")
			}
		}


	companion object {

		private val cache = ConcurrentHashMap<ParameterizedType, JSONCodableType<*>>()


		@PublishedApi
		@Suppress("UNCHECKED_CAST")
		internal fun <Type : Any> of(parameterizedType: ParameterizedType, typeArgumentIndex: Int = 0) =
			cache.getOrPut(parameterizedType) {
				parameterizedType.actualTypeArguments[typeArgumentIndex].toCodableType(
					upperBound = null,
					variance = KVariance.INVARIANT
				)
			} as JSONCodableType<Type>


		private fun Class<*>.toCodableType(upperBound: KClass<*>?, variance: KVariance): JSONCodableType<*> =
			if (isArray && !componentType.isPrimitive)
				JSONCodableType(
					rawClass = Array<Any?>::class,
					arguments = listOf(componentType.toCodableType(upperBound = Any::class, variance = KVariance.INVARIANT)),
					upperBound = upperBound,
					variance = variance
				)
			else
				JSONCodableType(
					rawClass = kotlin,
					arguments = emptyList(),
					upperBound = upperBound,
					variance = variance
				)


		private fun GenericArrayType.toCodableType(upperBound: KClass<*>?, variance: KVariance) =
			JSONCodableType(
				rawClass = Array<Any?>::class,
				arguments = listOf(genericComponentType.toCodableType(
					upperBound = null,
					variance = KVariance.INVARIANT
				)),
				upperBound = upperBound,
				variance = variance
			)


		private fun ParameterizedType.toCodableType(upperBound: KClass<*>?, variance: KVariance): JSONCodableType<*> {
			val rawClass = (rawType as Class<*>)

			return JSONCodableType(
				rawClass = rawClass.kotlin,
				arguments = actualTypeArguments.mapIndexed { index, type ->
					type.toCodableType(
						upperBound = rawClass.typeParameters[index].bounds.single().toRawClass().kotlin,
						variance = KVariance.INVARIANT
					)
				},
				upperBound = upperBound,
				variance = variance
			)
		}


		private fun WildcardType.toCodableType(upperBound: KClass<*>?) =
			when {
				lowerBounds.isNotEmpty() -> lowerBounds.single().toCodableType(
					upperBound = upperBound,
					variance = KVariance.IN
				)

				upperBounds.isNotEmpty() -> upperBounds.single().toCodableType(
					upperBound = upperBound,
					variance = KVariance.OUT
				)

				else -> error("impossible")
			}


		private fun Type.toCodableType(upperBound: KClass<*>?, variance: KVariance): JSONCodableType<*> =
			when (this) {
				is Class<*> -> toCodableType(upperBound = upperBound, variance = variance)
				is ParameterizedType -> toCodableType(upperBound = upperBound, variance = variance)
				is WildcardType -> toCodableType(upperBound = upperBound)
				is GenericArrayType -> toCodableType(upperBound = upperBound, variance = variance)
				else -> error("Unsupported type ${this::class.qualifiedName}: $this")
			}


		private fun Type.toRawClass() =
			when (this) {
				is Class<*> -> this
				is ParameterizedType -> rawType as Class<*>
				is GenericArrayType -> Array<Any?>::class.java
				else -> error("Unsupported type ${this::class.qualifiedName}: $this")
			}
	}
}


@PublishedApi
@Suppress("unused")
internal abstract class JSONCodableTypeReference<Type : Any>
