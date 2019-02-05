package com.github.fluidsonic.fluid.json.annotationprocessor

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract


internal inline fun <T> T.applyIf(condition: Boolean, block: T.() -> Unit): T {
	contract {
		callsInPlace(block, InvocationKind.AT_MOST_ONCE)
	}

	if (condition)
		block()

	return this
}
