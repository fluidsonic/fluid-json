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


fun <Type : Any> jsonCodableType(clazz: KClass<Type>) =
	JSONCodableType.of(clazz)


@JvmName("jsonCodableTypeOfReference")
fun <Type : Any> jsonCodableType(clazz: KClass<out JSONCodableTypeReference<out Type>>): JSONCodableType<Type> {
	val javaClass = clazz.java
	require(javaClass.superclass == JSONCodableTypeReference::class.java) { "An immediate subclass of ${JSONCodableTypeReference::class.simpleName} must be passed" }

	return JSONCodableType.of(javaClass.genericSuperclass as ParameterizedType)
}


@Suppress("unused")
class JSONCodableType<Type : Any> private constructor(
	val rawClass: KClass<Type>,
	val arguments: List<JSONCodableType<*>>,
	upperBoundInGenericContext: KClass<*>?,
	varianceInGenericContext: KVariance
) {

	private val hashCode =
		rawClass.hashCode() xor arguments.hashCode()


	internal val isUnconstrainedUpperBoundInGenericContext =
		(rawClass == upperBoundInGenericContext && (varianceInGenericContext == KVariance.OUT || varianceInGenericContext == KVariance.INVARIANT))


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
					if (it.isUnconstrainedUpperBoundInGenericContext) "*" else it.toString()
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


		internal fun <Type : Any> of(clazz: KClass<Type>) =
			clazz.java.toCodableType(upperBound = null, variance = KVariance.INVARIANT)


		private fun <Type : Any> Class<Type>.toCodableType(upperBound: KClass<*>?, variance: KVariance): JSONCodableType<Type> =
			if (isArray && !componentType.isPrimitive)
				JSONCodableType(
					rawClass = kotlin,
					arguments = listOf(componentType.toCodableType(upperBound = Any::class, variance = KVariance.INVARIANT)),
					upperBoundInGenericContext = upperBound,
					varianceInGenericContext = variance
				)
			else
				JSONCodableType(
					rawClass = kotlin,
					arguments = typeParameters.map { it.bounds.first().toCodableType(upperBound = null, variance = KVariance.INVARIANT) },
					upperBoundInGenericContext = upperBound,
					varianceInGenericContext = variance
				)


		private fun GenericArrayType.toCodableType(upperBound: KClass<*>?, variance: KVariance) =
			JSONCodableType(
				rawClass = Array<Any?>::class,
				arguments = listOf(genericComponentType.toCodableType(
					upperBound = null,
					variance = KVariance.INVARIANT
				)),
				upperBoundInGenericContext = upperBound,
				varianceInGenericContext = variance
			)


		private fun ParameterizedType.toCodableType(upperBound: KClass<*>?, variance: KVariance): JSONCodableType<*> {
			val rawClass = (rawType as Class<*>)

			return JSONCodableType(
				rawClass = rawClass.kotlin,
				arguments = actualTypeArguments.mapIndexed { index, type ->
					type.toCodableType(
						upperBound = rawClass.typeParameters[index].bounds.first().toRawClass().kotlin,
						variance = KVariance.INVARIANT
					)
				},
				upperBoundInGenericContext = upperBound,
				varianceInGenericContext = variance
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


		@Suppress("UNCHECKED_CAST")
		private fun Type.toCodableType(upperBound: KClass<*>?, variance: KVariance): JSONCodableType<*> =
			when (this) {
				is Class<*> -> (this as Class<Any>).toCodableType(upperBound = upperBound, variance = variance)
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


@Suppress("unused")
abstract class JSONCodableTypeReference<Type : Any>
