package com.github.fluidsonic.fluid.json.annotationprocessor

import com.github.fluidsonic.fluid.json.*
import com.github.fluidsonic.fluid.meta.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.STAR
import javax.lang.model.element.Element


// TODO disallow INTERNAL visibility for external types
internal class ProcessingPhase(
	private val collectionResult: CollectionResult,
	private val errorLogger: ErrorLogger
) {

	private val codecs: MutableCollection<ProcessingResult.Codec> = mutableListOf()
	private var codecProvider: ProcessingResult.CodecProvider? = null
	private var isProcessed = false


	private fun decodingStrategyForType(type: CollectionResult.Type): ProcessingResult.Codec.DecodingStrategy? {
		val typeParameters = (type.meta as? MGeneralizable)?.typeParameters.orEmpty()

		fun decodablePropertiesForConstructor(meta: MConstructor) =
			meta.valueParameters
				.map { parameter ->
					if (parameter.isVariadic) fail("constructor used for decoding must not use vararg:\n$meta")

					val parameterName = parameter.name
					val parameterType = parameter.type

					val annotation = type.decodableProperties[parameterName]
					val encodingAnnotation = type.properties[parameterName]
					val (parameterKotlinpoetType, typeParameterIndex) = when (parameterType) {
						is MTypeReference.Class -> parameterType.forKotlinPoet(typeParameters = typeParameters) to -1
						is MTypeReference.TypeParameter -> typeParameters.first { it.id == parameterType.id }.let { typeParameter ->
							val upperBound = typeParameter.upperBounds.firstOrNull()?.forKotlinPoet(typeParameters = emptyList())
								?: KotlinpoetTypeNames.nullableAny

							upperBound to typeParameters.indexOfFirst { it.id == parameterType.id }
						}
						else -> error("not possible")
					}

					ProcessingResult.Codec.DecodableProperty(
						name = parameterName,
						presenceRequired = parameterKotlinpoetType.isPrimitive, // not yet supported for non-primitive types
						serializedName = annotation?.annotation?.serializedName?.takeIf { it != "<automatic>" }
							?: encodingAnnotation?.annotation?.serializedName?.takeIf { it != "<automatic>" }
							?: parameterName.toString(),
						type = parameterKotlinpoetType,
						typeParameterIndex = typeParameterIndex
					)
				}
				.also { properties ->
					properties.groupBy { it.serializedName }
						.forEach { (serializedName, propertiesHavingThatName) ->
							if (propertiesHavingThatName.size > 1)
								fail("multiple decodable properties (constructor value parameters) have the same serialized name '$serializedName':\n" +
									propertiesHavingThatName.joinToString { it.name.kotlin }
								)
						}
				}


		fun decodingStrategyForConstructor(meta: MConstructor) =
			ProcessingResult.Codec.DecodingStrategy(
				meta = meta,
				properties = decodablePropertiesForConstructor(meta)
			)


		fun MConstructor.isSuitableForAutomaticSelection() =
			(visibility == MVisibility.INTERNAL || visibility == MVisibility.PUBLIC) &&
				!type.constructorExclusions.containsKey(localId)


		if (type.constructor != null && type.constructorExclusions.containsKey(type.constructor.meta.localId))
			fail("cannot use @JSON.Constructor and @JSON.Excluded on the same constructor")

		val strategy = when (type.meta) {
			is MClass -> when (type.annotation.decoding) {
				JSON.Decoding.annotatedConstructor ->
					type.constructor?.meta
						?.let(::decodingStrategyForConstructor)
						?: fail("type uses JSON.Decoding.annotatedConstructor but none was annotated with @JSON.Constructor")

				JSON.Decoding.automatic ->
					(type.constructor?.meta
						?: type.meta.primaryConstructor?.takeIf(MConstructor::isSuitableForAutomaticSelection)
						?: type.meta.constructors
							.filter(MConstructor::isSuitableForAutomaticSelection)
							.ifEmpty { null }
							?.let { candidates ->
								candidates.singleOrNull()
									?: fail("multiple secondary constructors could be used for decoding so one must be marked explicitly with @JSON.Constructor")
							}
						)
						?.let(::decodingStrategyForConstructor)
						?: fail("cannot find a constructor suitable for decoding so one should be provided or decoding be disabled using JSON.Decoding.none")

				JSON.Decoding.none ->
					null

				JSON.Decoding.primaryConstructor ->
					type.meta.primaryConstructor
						?.let(::decodingStrategyForConstructor)
						?: fail("type uses @JSON.Decoding.primaryConstructor but has no primary constructor")
			}

			is MObject -> when (type.annotation.decoding) {
				JSON.Decoding.annotatedConstructor ->
					fail("cannot use JSON.Decoding.annotatedConstructor with objects")

				JSON.Decoding.automatic,
				JSON.Decoding.none ->
					null

				JSON.Decoding.primaryConstructor ->
					fail("cannot use JSON.Decoding.primaryConstructor with objects")
			}

			else -> fail("cannot use @JSON on this type")
		}

		if (strategy != null) {
			if (type.constructor != null && type.constructor.meta.localId != strategy.meta.localId)
				fail("type uses JSON.Decoding.${type.annotation.decoding.name} but annotated a different constructor with @JSON.Constructor")

			if (type.constructorExclusions.containsKey(strategy.meta.localId))
				fail("type uses JSON.Decoding.${type.annotation.decoding.name} but selected constructor is annotated with @JSON.Excluded")
		}
		else {
			if (type.constructor != null)
				fail("type is supposed to not be decodable but a constructor was annotated with @JSON.Constructor")
		}

		return strategy
	}


	private fun encodingStrategyForType(type: CollectionResult.Type, decodingStrategy: ProcessingResult.Codec.DecodingStrategy?): ProcessingResult.Codec.EncodingStrategy? {
		val typeParameters = (type.meta as? MGeneralizable)?.typeParameters.orEmpty()

		fun encodingStrategyForProperties(properties: Collection<MProperty>) = ProcessingResult.Codec.EncodingStrategy(
			customPropertyMethods = type.customProperties.map { it.extensionPackageName to it.functionMeta.name },
			properties = properties
				.filterNot { type.propertyExclusions.containsKey(it.name) }
				.associateBy { it.localId } // de-duplicate
				.values
				.map { property ->
					val annotation = type.properties[property.name]
					val decodingAnnotation = type.decodableProperties[property.name]

					ProcessingResult.Codec.EncodableProperty(
						importPackageName = annotation?.extensionPackageName,
						name = property.name,
						serializedName = annotation?.annotation?.serializedName?.takeIf { it != "<automatic>" }
							?: decodingAnnotation?.annotation?.serializedName?.takeIf { it != "<automatic>" }
							?: property.name.toString(),
						type = property.getter.returnType.forKotlinPoet(typeParameters = typeParameters)
					) to property
				}
				.let { propertiesAndMeta ->
					propertiesAndMeta
						.groupBy { it.first.serializedName }
						.map { (serializedName, propertiesHavingThatName) ->
							if (propertiesHavingThatName.size > 1)
								fail("multiple encodable properties have the same serialized name '$serializedName':\n" +
									propertiesHavingThatName.joinToString("\n") { (property, propertyMeta) ->
										when (val extensionPackageName = property.importPackageName) {
											null -> "// in ${type.meta.name}\n$propertyMeta"
											else -> "// in package $extensionPackageName\n$propertyMeta"
										}
									})

							propertiesHavingThatName.first().first
						}
				}
		)


		fun MProperty.isSuitableForAutomaticSelection(defaultOnly: Boolean) =
			(visibility == MVisibility.INTERNAL || visibility == MVisibility.PUBLIC)
				&& (!defaultOnly || getter.isDefault)
				&& source == MClassMemberSource.DECLARATION
				&& receiverParameterType == null


		val metaProperties = (type.meta as? MPropertyContainer)?.properties
			?: fail("cannot use @JSON on this type")

		val strategy = when (type.annotation.encoding) {
			JSON.Encoding.allProperties ->
				encodingStrategyForProperties(
					metaProperties.filter { it.isSuitableForAutomaticSelection(defaultOnly = false) } +
						type.properties.values.map { it.meta }
				)

			JSON.Encoding.annotatedProperties ->
				encodingStrategyForProperties(
					metaProperties.filter { type.decodableProperties.containsKey(it.name) } +
						type.properties.values.map { it.meta }
				)

			JSON.Encoding.automatic ->
				encodingStrategyForProperties(
					metaProperties.filter { it.isSuitableForAutomaticSelection(defaultOnly = true) } +
						type.properties.values.map { it.meta }
				)

			JSON.Encoding.none ->
				null
		}

		if (strategy == null) {
			if (type.customProperties.isNotEmpty())
				fail("type is supposed to not be encodable but methods have been annotated with @JSON.CustomProperties")

			val encodableOnlyProperties = when (decodingStrategy) {
				null -> type.properties.values
				else -> type.properties.values
					.filterNot { property -> decodingStrategy.properties.any { it.name == property.meta.name } }
			}
			if (encodableOnlyProperties.isNotEmpty())
				fail("type is supposed to not be encodable but properties have been annotated with @JSON.Property")
		}

		return strategy
	}


	private fun fail(message: String): Nothing =
		throw Fail(message)


	fun process(): ProcessingResult {
		check(!isProcessed) { "can only process once" }
		isProcessed = true

		process(collectionResult.codecProviders)
		process(collectionResult.types)

		return ProcessingResult(
			codecs = codecs,
			codecProvider = codecProvider
		)
	}


	@JvmName("processCodecProviders")
	private fun process(results: Collection<CollectionResult.CodecProvider>) {
		for (result in results) {
			withFailureHandling(annotation = "JSON.CodecProvider", element = result.element) {
				process(result)
			}
		}
	}


	@JvmName("processTypes")
	private fun process(results: Collection<CollectionResult.Type>) {
		for (result in results) {
			withFailureHandling(annotation = "JSON", element = result.element) {
				process(result)
			}
		}
	}


	private fun process(codecProvider: CollectionResult.CodecProvider) {
		if (this.codecProvider != null)
			fail(
				collectionResult.codecProviders.joinToString(
					prefix = "generating multiple codec providers is not supported:\n",
					separator = "\n"
				) { it.interfaceMeta.name.toString() }
			)

		this.codecProvider = ProcessingResult.CodecProvider(
			contextType = codecProvider.contextType,
			interfaceType = codecProvider.supertype,
			isPublic = codecProvider.visibility == MVisibility.PUBLIC,
			name = codecProvider.interfaceMeta.name
		)
	}


	private fun process(type: CollectionResult.Type) {
		val annotation = type.annotation
		val meta = type.meta as? MNamedType ?: fail("cannot use @JSON on this type")

		val decodingStrategy = decodingStrategyForType(type)
		val encodingStrategy = encodingStrategyForType(type, decodingStrategy = decodingStrategy)
		if (decodingStrategy == null && encodingStrategy == null)
			fail("type is neither decodable nor encodable")

		val isSingleValue = when (annotation.representation) {
			JSON.Representation.automatic -> meta is MInlineable && meta.isInline
			JSON.Representation.structured -> false
			JSON.Representation.singleValue -> true
		}
		if (isSingleValue) {
			if (decodingStrategy != null && decodingStrategy.properties.size != 1)
				fail("class with JSON.Representation.singleValue must have exactly one decodable property")

			if (encodingStrategy != null) {
				if (encodingStrategy.properties.size != 1)
					fail("class with JSON.Representation.singleValue must have exactly one encodable property")

				if (encodingStrategy.customPropertyMethods.isNotEmpty())
					fail("class with JSON.Representation.singleValue cannot use @JSON.CustomProperties")
			}
		}

		val isPublic = when (annotation.codecVisibility) {
			JSON.Visibility.automatic -> type.actualVisibility == MVisibility.PUBLIC
			JSON.Visibility.internal -> false
			JSON.Visibility.publicRequired -> true
		}

		codecs += ProcessingResult.Codec(
			contextType = codecProvider?.contextType?.name ?: TypeNames.codingContext,
			decodingStrategy = decodingStrategy,
			encodingStrategy = encodingStrategy,
			isSingleValue = isSingleValue,
			isPublic = isPublic,
			name = MQualifiedTypeName.fromKotlin(
				packageName = annotation.codecPackageName
					.takeIf { it != "<automatic>" }
					?: type.preferredCodecPackageName?.kotlin
					?: meta.name.packageName.kotlin,
				typeName = annotation.codecName
					.takeIf { it != "<automatic>" }
					?: meta.name.withoutPackage().kotlin.replace('.', '_') + "JSONCodec"
			),
			valueType = meta.name.forKotlinPoet().let {
				if (meta is MGeneralizable && meta.typeParameters.isNotEmpty()) it.parameterizedBy(*Array(meta.typeParameters.size) { STAR })
				else it
			}
		)
	}


	private inline fun withFailureHandling(annotation: String, element: Element, block: () -> Unit) {
		try {
			block()
		}
		catch (e: Fail) {
			errorLogger.logError("@$annotation on ${element.fullyQualifiedName}: ${e.message}")
		}
	}


	private class Fail(override val message: String) : RuntimeException(message)
}
