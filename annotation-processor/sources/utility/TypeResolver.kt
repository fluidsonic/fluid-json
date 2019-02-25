package com.github.fluidsonic.fluid.json.annotationprocessor

import javax.lang.model.element.TypeElement


internal interface TypeResolver {

	fun resolveType(qualifiedName: String): TypeElement?
}
