package io.fluidsonic.json.annotationprocessor

import io.fluidsonic.json.*
import io.fluidsonic.meta.*
import javax.lang.model.element.*


internal data class CollectionResult(
	val codecProviders: Collection<CodecProvider>,
	val types: Collection<Type>
) {

	data class CodecProvider(
		val annotation: Json.CodecProvider,
		val contextType: MTypeReference,
		val element: TypeElement,
		val interfaceMeta: MInterface,
		val supertype: MTypeReference,
		val visibility: MVisibility
	)


	data class Constructor(
		val annotation: Json.Constructor,
		val element: ExecutableElement,
		val meta: MConstructor
	)


	data class ConstructorExclusion(
		val annotation: Json.Excluded,
		val meta: MConstructor,
		val element: ExecutableElement
	)


	data class CustomProperties(
		val annotation: Json.CustomProperties,
		val element: ExecutableElement,
		val extensionPackageName: MPackageName?,
		val functionMeta: MFunction
	)


	data class DecodableProperty(
		val annotation: Json.Property,
		val element: VariableElement,
		val meta: MValueParameter
	)


	data class Property(
		val annotation: Json.Property,
		val element: ExecutableElement,
		val extensionPackageName: MPackageName?,
		val meta: MProperty
	)


	data class PropertyExclusion(
		val annotation: Json.Excluded,
		val meta: MProperty,
		val element: ExecutableElement
	)


	data class Type(
		val actualVisibility: MVisibility,
		val annotation: Json,
		val constructor: Constructor?,
		val constructorExclusions: Map<MLocalId.Constructor, ConstructorExclusion>,
		val customProperties: Collection<CustomProperties>,
		val decodableProperties: Map<MVariableName, DecodableProperty>,
		val element: TypeElement,
		val meta: MNamedType,
		val preferredCodecPackageName: MPackageName?,
		val properties: Map<MVariableName, Property>,
		val propertyExclusions: Map<MVariableName, PropertyExclusion>
	)
}
