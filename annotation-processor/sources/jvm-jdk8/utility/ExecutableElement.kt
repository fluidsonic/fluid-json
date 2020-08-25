package io.fluidsonic.json.annotationprocessor

import javax.lang.model.element.*


internal val ExecutableElement.jvmMethodSignature
	get() = jvmMethodSignature(name = simpleName.toString(), parameterTypes = parameters.map { it.asType() }, returnType = returnType)
