@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")

package io.fluidsonic.json

import kotlin.Boolean
import kotlin.Byte
import kotlin.Char
import kotlin.Double
import kotlin.Float
import kotlin.Int
import kotlin.Long
import kotlin.Short
import kotlin.reflect.*
import java.lang.Boolean as BoxedBoolean
import java.lang.Byte as BoxedByte
import java.lang.Character as BoxedCharacter
import java.lang.Double as BoxedDouble
import java.lang.Float as BoxedFloat
import java.lang.Integer as BoxedInteger
import java.lang.Long as BoxedLong
import java.lang.Short as BoxedShort
import java.lang.Void as BoxedVoid

// this will get tricky when adding multi-platform support!


internal val KClass<*>.boxed
	get() =
		if (java.isPrimitive)
			when (this) {
				Boolean::class -> BoxedBoolean::class
				Byte::class -> BoxedByte::class
				Char::class -> BoxedCharacter::class
				Double::class -> BoxedDouble::class
				Float::class -> BoxedFloat::class
				Int::class -> BoxedInteger::class
				Long::class -> BoxedLong::class
				Short::class -> BoxedShort::class
				else -> BoxedVoid::class
			}
		else
			this


internal fun KClass<*>.isAssignableOrBoxableTo(otherClass: KClass<*>) =
	otherClass.isAssignableOrBoxableFrom(this)


internal fun KClass<*>.isAssignableOrBoxableFrom(otherClass: KClass<*>) =
	boxed.java.isAssignableFrom(otherClass.boxed.java)


internal fun <T : Any, U : Any> KClass<T>.takeIfSubclassOf(superclass: KClass<U>) =
	takeIf { isAssignableOrBoxableTo(superclass) }


internal fun <T : Any, U : Any> KClass<T>.takeIfSuperclassOf(subclass: KClass<U>) =
	takeIf { subclass.isAssignableOrBoxableTo(this) }
