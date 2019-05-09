package com.github.fluidsonic.fluid.json

import kotlin.reflect.*
import kotlin.reflect.jvm.internal.*

// from kotlin-reflect, modified, probably wrong in some edge cases, close enough in order to avoid kotlin-reflect dependency for now


/**
 * Returns the [KClass] instance representing the runtime class to which this type is erased to on JVM.
 */
@SinceKotlin("1.1")
val KType.jvmErasure: KClass<*>
	get() = classifier?.jvmErasure ?: throw KotlinReflectionInternalError("Cannot calculate JVM erasure for type: $this")


internal val KClassifier.jvmErasure: KClass<*>
	get() = when (this) {
		is KClass<*> -> this
		is KTypeParameter -> {
			val bounds = upperBounds
			val representativeBoundErasures = bounds.mapNotNull { it.classifier?.jvmErasure }
			representativeBoundErasures.firstOrNull { !(it.java.isInterface || it.java.isAnnotation) }
				?: representativeBoundErasures.firstOrNull()
				?: Any::class
		}
		else -> throw KotlinReflectionInternalError("Cannot calculate JVM erasure for type: $this")
	}
