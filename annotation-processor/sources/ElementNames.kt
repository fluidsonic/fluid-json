package com.github.fluidsonic.fluid.json.annotationprocessor

import com.github.fluidsonic.fluid.json.*
import com.github.fluidsonic.fluid.meta.*


internal object ElementNames {

	val anyType = MQualifiedTypeName.from(Any::class)
	val codecProviderType = MQualifiedTypeName.from(JSONCodecProvider::class)
	val codingContext = MQualifiedTypeName.from(JSONCodingContext::class)
	val encoder = MQualifiedTypeName.from(JSONEncoder::class)
}
