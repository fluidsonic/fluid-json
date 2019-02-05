package com.github.fluidsonic.fluid.json.annotationprocessor

import com.github.fluidsonic.fluid.meta.*
import com.squareup.kotlinpoet.TypeName


internal data class CodecProviderConfiguration(
	val contextType: MQualifiedTypeName,
	val interfaceType: TypeName,
	val isPublic: Boolean,
	val name: MQualifiedTypeName
)
