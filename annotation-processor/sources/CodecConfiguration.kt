package com.github.fluidsonic.fluid.json.annotationprocessor

import com.github.fluidsonic.fluid.meta.*
import com.squareup.kotlinpoet.TypeName


internal data class CodecConfiguration(
	val decodableProperties: List<DecodableProperty>,
	val encodableProperties: List<EncodableProperty>,
	val isPublic: Boolean,
	val name: MQualifiedTypeName,
	val typeName: TypeName
) {

	data class DecodableProperty(
		val name: MVariableName,
		val presenceRequired: Boolean,
		val serializedName: String,
		val type: TypeName
	)


	data class EncodableProperty(
		val name: MVariableName,
		val serializedName: String,
		val type: TypeName
	)
}
