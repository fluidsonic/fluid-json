package io.fluidsonic.json.annotationprocessor

import io.fluidsonic.meta.*
import javax.lang.model.*


internal fun AnnotatedConstruct.getAnnotationMirror(typeName: MQualifiedTypeName) =
	typeName.kotlin.let { jvmTypeName ->
		annotationMirrors.firstOrNull { it.annotationType.toString() == jvmTypeName }
	}
