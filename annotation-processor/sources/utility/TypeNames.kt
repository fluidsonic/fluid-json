package com.github.fluidsonic.fluid.json.annotationprocessor

import com.github.fluidsonic.fluid.json.*
import com.github.fluidsonic.fluid.meta.*


internal object TypeNames {

	val anyType = MQualifiedTypeName.of(Any::class)
	val codecProviderType = MQualifiedTypeName.of(JSONCodecProvider::class)
	val codingContext = MQualifiedTypeName.of(JSONCodingContext::class)
	val encoder = MQualifiedTypeName.of(JSONEncoder::class)
}
