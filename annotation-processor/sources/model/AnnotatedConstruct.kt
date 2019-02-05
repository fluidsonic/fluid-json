package com.github.fluidsonic.fluid.json.annotationprocessor

import javax.lang.model.AnnotatedConstruct
import javax.lang.model.type.DeclaredType


internal fun AnnotatedConstruct.getAnnotationMirror(type: DeclaredType) =
	type.toString().let { typeName ->
		annotationMirrors.firstOrNull { it.annotationType.toString() == typeName }
	}
