package com.github.fluidsonic.fluid.json.annotationprocessor

import com.github.fluidsonic.fluid.meta.*
import javax.lang.model.AnnotatedConstruct


internal fun AnnotatedConstruct.getAnnotationMirror(typeName: MQualifiedTypeName) =
	typeName.jvmInternal.let { jvmTypeName ->
		annotationMirrors.firstOrNull { it.annotationType.toString() == jvmTypeName }
	}
