package com.github.fluidsonic.fluid.json.annotationprocessor

import javax.lang.model.element.*


internal interface TypeResolver {

	fun resolveType(qualifiedName: String): TypeElement?
}
