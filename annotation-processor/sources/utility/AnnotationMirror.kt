package com.github.fluidsonic.fluid.json.annotationprocessor

import javax.lang.model.element.AnnotationMirror


internal inline fun <reified T : Any> AnnotationMirror.getValue(key: String) =
	elementValues.entries
		.firstOrNull { it.key.simpleName.toString() == key }
		?.value
		?.value
		as? T
