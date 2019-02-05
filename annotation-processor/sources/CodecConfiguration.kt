package com.github.fluidsonic.fluid.json.annotationprocessor

import com.github.fluidsonic.fluid.meta.*
import com.squareup.kotlinpoet.TypeName


internal data class CodecConfiguration(
	val contextType: MQualifiedTypeName,
	val customPropertyMethods: List<Pair<MPackageName, MFunctionName>>,
	val decodableProperties: List<DecodableProperty>,
	val encodableProperties: List<EncodableProperty>,
	val isDecodable: Boolean,
	val isEncodable: Boolean,
	val isObject: Boolean,
	val isPublic: Boolean,
	val name: MQualifiedTypeName,
	val valueTypeName: MQualifiedTypeName
) {

	data class DecodableProperty(
		val isNullable: Boolean,
		val name: MVariableName,
		val presenceRequired: Boolean,
		val serializedName: String,
		val type: TypeName
	)


	data class EncodableProperty(
		val importPackageName: MPackageName?,
		val isNullable: Boolean,
		val name: MVariableName,
		val serializedName: String,
		val type: TypeName
	)
}
