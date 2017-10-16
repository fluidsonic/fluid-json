package com.github.fluidsonic.fluid.json


// from java.lang.Math.multiplyExact()
internal fun Long.Companion.canMultiplyWithoutOverflow(x: Long, y: Long): Boolean {
	val r = x * y
	val ax = Math.abs(x)
	val ay = Math.abs(y)

	if ((ax or ay) ushr 31 != 0L) {
		if ((y != 0L && r / y != x) || (x == MIN_VALUE && y == -1L)) {
			return false
		}
	}

	return true
}
