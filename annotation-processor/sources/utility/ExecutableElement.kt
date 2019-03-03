package com.github.fluidsonic.fluid.json.annotationprocessor

import javax.lang.model.element.ExecutableElement


internal val ExecutableElement.jvmMethodSignature
	get() = jvmMethodSignature(name = simpleName.toString(), parameterTypes = parameters.map { it.asType() }, returnType = returnType)