package com.github.fluidsonic.fluid.json.annotationprocessor

import com.github.fluidsonic.fluid.meta.*
import com.squareup.kotlinpoet.*


internal data class ProcessingResult(
	val codecs: Collection<Codec>,
	val codecProvider: CodecProvider?
) {

	internal data class Codec(
		val contextType: MQualifiedTypeName,
		val decodingStrategy: DecodingStrategy?,
		val encodingStrategy: EncodingStrategy?,
		val isPublic: Boolean,
		val isSingleValue: Boolean,
		val name: MQualifiedTypeName,
		val valueType: TypeName
	) {

		data class DecodableProperty(
			val defaultValue: DefaultValue,
			val name: MVariableName,
			val serializedName: String,
			val type: TypeName,
			val typeParameterIndex: Int
		) {

			enum class DefaultValue {

				defaultArgument,
				none,
				nullReference
			}
		}


		data class DecodingStrategy(
			val meta: MConstructor,
			val properties: Collection<DecodableProperty>
		)


		data class EncodableProperty(
			val importPackageName: MPackageName?,
			val name: MVariableName,
			val serializedName: String,
			val type: TypeName
		)


		data class EncodingStrategy(
			val customPropertyMethods: Collection<Pair<MPackageName?, MFunctionName>>,
			val properties: Collection<EncodableProperty>
		)
	}


	data class CodecProvider(
		val contextType: MTypeReference,
		val interfaceType: MTypeReference,
		val isPublic: Boolean,
		val name: MQualifiedTypeName
	)
}
