package com.github.fluidsonic.fluid.json


internal val Class<*>.boxed
	get() =
		if (isPrimitive)
			when (this) {
				Boolean::class.java -> java.lang.Boolean::class.java
				Byte::class.java -> java.lang.Byte::class.java
				Char::class.java -> java.lang.Character::class.java
				Double::class.java -> java.lang.Double::class.java
				Float::class.java -> java.lang.Float::class.java
				Int::class.java -> java.lang.Integer::class.java
				Long::class.java -> java.lang.Long::class.java
				Short::class.java -> java.lang.Short::class.java
				else -> java.lang.Void::class.java
			}
		else
			this


internal fun Class<*>.isAssignableOrBoxableTo(otherClass: Class<*>) =
	otherClass.isAssignableOrBoxableFrom(this)


internal fun Class<*>.isAssignableOrBoxableFrom(otherClass: Class<*>) =
	boxed.isAssignableFrom(otherClass.kotlin.javaObjectType)
