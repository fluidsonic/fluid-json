package io.fluidsonic.json

import java.lang.reflect.*
import java.util.concurrent.*
import kotlin.Array
import kotlin.reflect.*


inline fun <reified Type : Any> jsonCodingType(): JsonCodingType<Type> =
	object : JsonCodingTypeReference<Type>() {}.type


fun <Type : Any> jsonCodingType(clazz: KClass<Type>, vararg arguments: KClass<*>): JsonCodingType<Type> =
	JsonCodingType.of(clazz, arguments = * arguments)


@JvmName("jsonCodingTypeOfReference")
fun <Type : Any> jsonCodingType(clazz: KClass<out JsonCodingTypeReference<out Type>>): JsonCodingType<Type> {
	val javaClass = clazz.java
	require(javaClass.superclass == JsonCodingTypeReference::class.java) { "An immediate subclass of ${JsonCodingTypeReference::class.simpleName} must be passed" }

	@Suppress("UNCHECKED_CAST")
	return JsonCodingType.of((javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.first()!!) as JsonCodingType<Type>
}


class JsonCodingType<Type : Any> private constructor(
	val rawClass: KClass<Type>,
	val arguments: List<JsonCodingType<*>>,
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

		if (other !is JsonCodingType<*>) {
			return false
		}

		return rawClass == other.rawClass && arguments == other.arguments
	}


	override fun hashCode(): Int =
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

		private val cache = ConcurrentHashMap<Type, JsonCodingType<*>>()


		@PublishedApi
		@Suppress("UNCHECKED_CAST")
		internal fun of(type: Type): JsonCodingType<*> =
			cache.getOrPut(type) {
				type.toCodableType(
					upperBound = null,
					variance = KVariance.INVARIANT
				)
			}


		internal fun <Type : Any> of(clazz: KClass<Type>, vararg arguments: KClass<*>) =
			clazz.java.toCodableType(
				upperBound = null,
				variance = KVariance.INVARIANT,
				explicitArguments = arguments.ifEmpty { null }?.toList()
			)


		private fun <Type : Any> Class<Type>.toCodableType(
			upperBound: KClass<*>?,
			variance: KVariance,
			explicitArguments: List<KClass<*>>? = null
		): JsonCodingType<Type> =
			if (isArray && !componentType.isPrimitive)
				JsonCodingType(
					rawClass = kotlin,
					arguments =
					explicitArguments?.let { arguments ->
						check(arguments.size == 1) { "expecting exactly one type arguments for $this, got ${arguments.size}" }
						arguments.map { it.java.toCodableType(upperBound = Any::class, variance = KVariance.INVARIANT) }
					} ?: listOf(componentType.toCodableType(upperBound = Any::class, variance = KVariance.INVARIANT)),
					upperBoundInGenericContext = upperBound,
					varianceInGenericContext = variance
				)
			else
				JsonCodingType(
					rawClass = kotlin,
					arguments = explicitArguments
						?.let { arguments ->
							check(arguments.size == typeParameters.size) { "expecting exactly ${typeParameters.size} type arguments for $this, got ${arguments.size}" }

							arguments.mapIndexed { index, argument ->
								val typeParameter = typeParameters[index]

								check(typeParameter.bounds.all { it.toRawClass().kotlin.isAssignableOrBoxableFrom(argument) }) {
									"type parameter 2 ($argument) is not within its bounds for $this"
								}

								argument.java.toCodableType(
									upperBound = typeParameter.bounds.first().toRawClass().kotlin,
									variance = KVariance.INVARIANT
								)
							}
						}
						?: typeParameters.map {
							it.bounds.first().toCodableType(
								upperBound = it.bounds.first().toRawClass().kotlin,
								variance = KVariance.INVARIANT
							)
						},
					upperBoundInGenericContext = upperBound,
					varianceInGenericContext = variance
				)


		private fun GenericArrayType.toCodableType(upperBound: KClass<*>?, variance: KVariance) =
			JsonCodingType(
				rawClass = Array<Any?>::class,
				arguments = listOf(genericComponentType.toCodableType(
					upperBound = null,
					variance = KVariance.INVARIANT
				)),
				upperBoundInGenericContext = upperBound,
				varianceInGenericContext = variance
			)


		private fun ParameterizedType.toCodableType(upperBound: KClass<*>?, variance: KVariance): JsonCodingType<*> {
			val rawClass = (rawType as Class<*>)

			return JsonCodingType(
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


		@Suppress("unused")
		private fun TypeVariable<*>.toCodableType(upperBound: KClass<*>?) =
			Any::class.java.toCodableType(
				upperBound = upperBound,
				variance = KVariance.OUT
			)


		private fun WildcardType.toCodableType(upperBound: KClass<*>?) =
			when {
				lowerBounds.isNotEmpty() -> lowerBounds.single().toCodableType(
					upperBound = upperBound,
					variance = KVariance.IN
				)

				upperBounds.isNotEmpty() ->
					upperBounds.single().let { maximumUpperBound ->
						if (maximumUpperBound === Any::class.java && upperBound != null)
							upperBound.java.toCodableType(
								upperBound = upperBound,
								variance = KVariance.OUT
							)
						else
							maximumUpperBound.toCodableType(
								upperBound = upperBound,
								variance = KVariance.OUT
							)
					}


				else -> error("impossible")
			}


		@Suppress("UNCHECKED_CAST")
		private fun Type.toCodableType(upperBound: KClass<*>?, variance: KVariance): JsonCodingType<*> =
			when (this) {
				is Class<*> -> (this as Class<Any>).toCodableType(upperBound = upperBound, variance = variance)
				is ParameterizedType -> toCodableType(upperBound = upperBound, variance = variance)
				is WildcardType -> toCodableType(upperBound = upperBound)
				is TypeVariable<*> -> toCodableType(upperBound = upperBound)
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


abstract class JsonCodingTypeReference<Type : Any> {

	@Suppress("UNCHECKED_CAST")
	internal val type = JsonCodingType.of((this::class.java.genericSuperclass as ParameterizedType).actualTypeArguments.first()!!)
		as JsonCodingType<Type>
}
