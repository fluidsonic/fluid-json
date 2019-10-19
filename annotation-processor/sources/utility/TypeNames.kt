package io.fluidsonic.json.annotationprocessor

import io.fluidsonic.json.*
import io.fluidsonic.meta.*


internal object TypeNames {

	val anyType = MQualifiedTypeName.of(Any::class)
	val codecProviderType = MQualifiedTypeName.of(JsonCodecProvider::class)
	val codingContext = MQualifiedTypeName.of(JsonCodingContext::class)
	val encoder = MQualifiedTypeName.of(JsonEncoder::class)
}
