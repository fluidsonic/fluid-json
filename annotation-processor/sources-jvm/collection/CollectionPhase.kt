package io.fluidsonic.json.annotationprocessor

import io.fluidsonic.json.*
import io.fluidsonic.meta.*
import javax.annotation.processing.*
import javax.lang.model.element.*
import javax.lang.model.type.*
import kotlin.reflect.*


// TODO disallow INTERNAL visibility for external types of different module
internal class CollectionPhase(
	private val errorLogger: ErrorLogger,
	private val typeResolver: TypeResolver
) {

	val annotationClasses = setOf(
		Json::class,
		Json.CodecProvider::class,
		Json.Constructor::class,
		Json.CustomProperties::class,
		Json.Excluded::class,
		Json.Property::class
	)


	private val codecProviders: MutableCollection<CollectionResult.CodecProvider> = mutableListOf()
	private val types: MutableMap<MQualifiedTypeName, CollectedType> = mutableMapOf()


	fun collect(roundEnvironment: RoundEnvironment) {
		collect(Json::class, roundEnvironment = roundEnvironment, collector = this::collect)
		collect(Json.CodecProvider::class, roundEnvironment = roundEnvironment, collector = this::collect)
		collect(Json.Constructor::class, roundEnvironment = roundEnvironment, collector = this::collect)
		collect(Json.CustomProperties::class, roundEnvironment = roundEnvironment, collector = this::collect)
		collect(Json.Excluded::class, roundEnvironment = roundEnvironment, collector = this::collect)
		collect(Json.Property::class, roundEnvironment = roundEnvironment, collector = this::collect)
	}


	private inline fun <T : Annotation> collect(
		annotationClass: KClass<T>,
		roundEnvironment: RoundEnvironment,
		collector: (annotation: T, roundEnvironment: RoundEnvironment, element: Element) -> Unit
	) {
		for (element in roundEnvironment.getElementsAnnotatedWith(annotationClass.java)) {
			val annotation = element.getAnnotation(annotationClass.java)

			withFailureHandling(annotationClass = annotationClass, element = element) {
				collector(annotation, roundEnvironment, element)
			}
		}
	}


	private fun collect(annotation: Json, roundEnvironment: RoundEnvironment, element: Element) =
		collect(annotation = annotation, roundEnvironment = roundEnvironment, element = element, preferredCodecPackageName = null)


	@Suppress("UNUSED_PARAMETER")
	private fun collect(annotation: Json, roundEnvironment: RoundEnvironment, element: Element, preferredCodecPackageName: MPackageName?) {
		val meta = Meta.of(element)
			?: fail("must be a Kotlin type")

		if (meta !is MVisibilityRestrictable || meta !is MNamedType || (meta !is MClass && meta !is MObject) || element !is TypeElement)
			fail("must be a Kotlin class")

		if (meta is MClass) {
			if (meta.inheritanceRestriction == MInheritanceRestriction.ABSTRACT || meta.isSealed)
				fail("must not be abstract or sealed")

			if (meta.isInner)
				fail("must not be an inner class")

			if (meta.typeParameters.any { it.upperBounds.size > 1 })
				fail("must not have type arguments with multiple upper bounds")
		}

		when (val visibility = meta.visibility) {
			MVisibility.INTERNAL ->
				if (annotation.codecVisibility == Json.CodecVisibility.publicRequired)
					fail("must be public if codec is supposed to be public")

			MVisibility.PUBLIC ->
				Unit

			else ->
				fail("must be internal or public but is $visibility")
		}

		var actualVisibility = meta.visibility

		var enclosingElement = element.enclosingElement
		while (enclosingElement != null && enclosingElement !is PackageElement) {
			val enclosingType = (Meta.of(enclosingElement) ?: fail("its enclosing element '$enclosingElement' isn't a Kotlin type"))
				as? MVisibilityRestrictable ?: fail("its enclosing element '$enclosingElement' isn't a Kotlin class")

			when (val visibility = enclosingType.visibility) {
				MVisibility.INTERNAL -> {
					if (annotation.codecVisibility == Json.CodecVisibility.publicRequired)
						fail("must only be nested in public types if codec is supposed to be public but '$enclosingElement' is $visibility")

					actualVisibility = visibility
				}

				MVisibility.PUBLIC ->
					Unit

				else ->
					fail("must only be nested in internal or public types but '$enclosingElement' is $visibility")
			}

			enclosingElement = enclosingElement.enclosingElement
		}

		val type = getOrCreateType(meta.name)
		type.actualVisibility = actualVisibility
		type.annotation = annotation
		type.element = element
		type.meta = meta
		type.preferredCodecPackageName = preferredCodecPackageName
	}


	private fun collect(annotation: Json.CodecProvider, roundEnvironment: RoundEnvironment, element: Element) {
		val interfaceMeta = Meta.of(element)
			?: fail("must be a Kotlin type")

		if (interfaceMeta !is MInterface || element !is TypeElement)
			fail("must be a Kotlin interface, is ${interfaceMeta::class.simpleName}")

		if (interfaceMeta.visibility != MVisibility.INTERNAL && interfaceMeta.visibility != MVisibility.PUBLIC)
			fail("must have internal or public visibility but is ${interfaceMeta.visibility}")

		var enclosingElement = element.enclosingElement
		while (enclosingElement != null && enclosingElement !is PackageElement) {
			val enclosingType = (Meta.of(enclosingElement) ?: fail("its enclosing element '$enclosingElement' isn't a Kotlin type"))
				as? MVisibilityRestrictable ?: fail("its enclosing element '$enclosingElement' isn't a Kotlin class")

			if (enclosingType.visibility != MVisibility.INTERNAL && enclosingType.visibility != MVisibility.PUBLIC)
				fail("must only be nested in internal or public types but '$enclosingElement' is ${enclosingType.visibility}")

			enclosingElement = enclosingElement.enclosingElement
		}

		if (interfaceMeta.typeParameters.isNotEmpty())
			fail("must not be generic")

		val supertype = (interfaceMeta.supertypes.singleOrNull() as? MTypeReference.Class)
			?.takeIf { it.name == TypeNames.codecProviderType }
			?: fail(
				"must only extend the interface JsonCodecProvider, extends " + interfaceMeta.supertypes
					.filterNot { it.name == TypeNames.anyType }
					.joinToString { it.name?.toString() ?: "?" }
					.ifEmpty { "none" }
			)

		val contextType = (supertype.arguments.first() as MTypeArgument.Type).type

		codecProviders += CollectionResult.CodecProvider(
			annotation = annotation,
			contextType = contextType,
			element = element,
			interfaceMeta = interfaceMeta,
			supertype = supertype,
			visibility = interfaceMeta.visibility
		)

		@Suppress("UNCHECKED_CAST")
		element.getAnnotationMirror(MQualifiedTypeName.of(Json.CodecProvider::class))
			?.getValue<List<AnnotationMirror>>("externalTypes")
			?.forEachIndexed { index, externalTypeAnnotationMirror ->
				val externalTypeAnnotation = annotation.externalTypes[index]

				val externalType = externalTypeAnnotationMirror.getValue<TypeMirror>("target")
					?: error("cannot properly parse external type annotation mirror: $externalTypeAnnotationMirror")

				if (externalType !is ReferenceType && externalTypeAnnotation.targetName.isEmpty())
					error("Json.ExternalType can only be used for reference types, not for '$externalType' (if you are using an inline class you have to specify 'targetName' too)")

				val externalTypeName = externalTypeAnnotation.targetName.ifEmpty { externalType.toString() }
				val externalTypeElement = typeResolver.resolveType(externalTypeName)
					?: error("cannot find external type element for '$externalTypeName'")

				withFailureHandling(annotationClass = Json.ExternalType::class, element = externalTypeElement) {
					collect(
						annotation = externalTypeAnnotation.configuration,
						roundEnvironment = roundEnvironment,
						element = externalTypeElement,
						preferredCodecPackageName = interfaceMeta.name.packageName
					)
				}
			}
	}


	@Suppress("UNUSED_PARAMETER")
	private fun collect(annotation: Json.Constructor, roundEnvironment: RoundEnvironment, element: Element) {
		if (element !is ExecutableElement)
			fail("cannot be used on that element")

		val typeElement = element.enclosingElement
			as? TypeElement
			?: fail("cannot find enclosing element")

		val typeMeta = (Meta.of(typeElement) ?: fail("cannot find Kotlin metadata for enclosing element"))
			as? MClass ?: fail("can only be used for class constructors and properties")

		if (!element.simpleName.contentEquals("<init>"))
			fail("can only be used for class constructors")

		val jvmMethodSignature = element.jvmMethodSignature

		val meta = typeMeta.constructors
			.firstOrNull { it.jvmSignature.toString() == jvmMethodSignature }
			?: fail("cannot find Kotlin metadata for constructor")

		if (meta.visibility != MVisibility.INTERNAL && meta.visibility != MVisibility.PUBLIC)
			fail("must have internal or public visibility but is ${meta.visibility}")

		getOrCreateType(typeMeta.name).constructors +=
			CollectionResult.Constructor(
				annotation = annotation,
				element = element,
				meta = meta
			)
	}


	@Suppress("UNUSED_PARAMETER")
	private fun collect(annotation: Json.CustomProperties, roundEnvironment: RoundEnvironment, element: Element) {
		if (element !is ExecutableElement)
			fail("cannot be used on that element")

		val enclosingElement = element.enclosingElement
			?: fail("cannot find enclosing element")

		val enclosingMeta = Meta.of(enclosingElement) as? MFunctionContainer
			?: fail("cannot find Kotlin metadata for enclosing element")

		val jvmMethodSignature = element.jvmMethodSignature

		val functionMeta = enclosingMeta.functions
			.firstOrNull { it.jvmSignature.toString() == jvmMethodSignature }
			?: fail("cannot find Kotlin metadata for function")

		val receiverParameterType = functionMeta.receiverParameterType
			?: fail("can only be used for extension functions")

		if (receiverParameterType.name != TypeNames.encoder)
			fail("receiver must be ${TypeNames.encoder}")

		if (functionMeta.typeParameters.isNotEmpty())
			fail("must not be generic")

		if (functionMeta.isSuspend)
			fail("must not be suspend")

		if (functionMeta.visibility != MVisibility.INTERNAL && functionMeta.visibility != MVisibility.PUBLIC)
			fail("must have internal or public visibility but is ${functionMeta.visibility}")

		fun collect(typeName: MQualifiedTypeName, extensionPackageName: MPackageName?) {
			getOrCreateType(typeName).customProperties.getOrPut(functionMeta.name to (extensionPackageName != null)) { mutableListOf() }
				.add(CollectionResult.CustomProperties(
					annotation = annotation,
					element = element,
					extensionPackageName = extensionPackageName,
					functionMeta = functionMeta
				))
		}

		when (enclosingMeta) {
			is MClass -> collect(
				typeName = enclosingMeta.name,
				extensionPackageName = null
			)
			is MFile -> collect(
				typeName = functionMeta.valueParameters.singleOrNull()
					?.type?.name
					?: fail("must have exactly one parameter"),
				extensionPackageName = MPackageName.of(element)
			)
			is MObject -> collect(
				typeName = enclosingMeta.name,
				extensionPackageName = null
			)
			else -> fail("can only be used on functions of classes, objects and files")
		}
	}


	@Suppress("UNUSED_PARAMETER")
	private fun collect(annotation: Json.Excluded, roundEnvironment: RoundEnvironment, element: Element) {
		if (element !is ExecutableElement)
			fail("cannot be used on that element")

		val typeElement = element.enclosingElement
			?: fail("cannot find enclosing element")

		val typeMeta = Meta.of(typeElement) as? MPropertyContainer
			?: fail("cannot find Kotlin metadata for enclosing element")

		if (typeMeta is MFile)
			fail("cannot be used on properties declared at file-level")

		if (typeMeta !is MNamedType || (typeMeta !is MClass && typeMeta !is MObject))
			fail("can only be used for class constructors and properties")

		val jvmMethodSignature = element.jvmMethodSignature

		if (typeMeta is MConstructable && element.simpleName.contentEquals("<init>")) {
			if (element.getAnnotation(Json.Constructor::class.java) != null)
				fail("cannot be used along @Json.Constructor on the same constructor")

			val meta = typeMeta.constructors
				.firstOrNull { it.jvmSignature.toString() == jvmMethodSignature }
				?: fail("cannot find Kotlin metadata for constructor")

			getOrCreateType(typeMeta.name).constructorExclusions[meta.localId] =
				CollectionResult.ConstructorExclusion(
					annotation = annotation,
					element = element,
					meta = meta
				)
		}
		else {
			if (element.getAnnotation(Json.Property::class.java) != null)
				fail("cannot be used along @Json.Property on the same property")

			val meta = typeMeta.properties
				.firstOrNull { it.jvmSyntheticMethodForAnnotationsSignature?.toString() == jvmMethodSignature }
				?: fail("cannot find Kotlin metadata for property")

			if (meta.receiverParameterType != null)
				fail("must not be an extension property")

			if (meta.typeParameters.isNotEmpty())
				fail("must not be generic")

			getOrCreateType(typeMeta.name).propertyExclusions[meta.name] =
				CollectionResult.PropertyExclusion(
					annotation = annotation,
					element = element,
					meta = meta
				)
		}
	}


	private fun collect(annotation: Json.Property, roundEnvironment: RoundEnvironment, element: Element) {
		when (element) {
			is ExecutableElement -> collect(annotation = annotation, roundEnvironment = roundEnvironment, element = element)
			is VariableElement -> collect(annotation = annotation, roundEnvironment = roundEnvironment, element = element)
			else -> fail("cannot be used on that element")
		}
	}


	@Suppress("UNUSED_PARAMETER")
	private fun collect(annotation: Json.Property, roundEnvironment: RoundEnvironment, element: ExecutableElement) {
		val enclosingElement = element.enclosingElement
			?: fail("cannot find enclosing element")

		val enclosingElementMeta = Meta.of(enclosingElement) as? MPropertyContainer
			?: fail("cannot find Kotlin metadata for enclosing element")

		val elementJvmMethodSignature = element.jvmMethodSignature
		val meta = enclosingElementMeta.properties
			.firstOrNull { it.jvmSyntheticMethodForAnnotationsSignature?.toString() == elementJvmMethodSignature }
			?: fail("cannot find Kotlin metadata for property")

		if (meta.source != MClassMemberSource.DECLARATION)
			return

		if (meta.typeParameters.isNotEmpty())
			fail("must not be generic")

		if (meta.visibility != MVisibility.INTERNAL && meta.visibility != MVisibility.PUBLIC)
			fail("must have internal or public visibility but is ${meta.visibility}")

		val extensionPackageName: MPackageName?
		val typeName: MQualifiedTypeName

		when (enclosingElementMeta) {
			is MFile -> {
				extensionPackageName = MPackageName.of(element)
				typeName = meta.receiverParameterType?.name ?: fail("can only be used for class and extension properties")
			}

			is MClass -> {
				extensionPackageName = null
				typeName = enclosingElementMeta.name
			}

			is MObject -> {
				extensionPackageName = null
				typeName = enclosingElementMeta.name
			}

			else ->
				fail("unexpected enclosing element of property: $enclosingElement (${enclosingElementMeta::class.simpleName})")
		}

		if (extensionPackageName == null && meta.receiverParameterType != null)
			fail("must not have a receiver parameter")

		getOrCreateType(typeName).properties.getOrPut(meta.name) { mutableListOf() }
			.add(CollectionResult.Property(
				annotation = annotation,
				element = element,
				extensionPackageName = extensionPackageName,
				meta = meta
			))
	}


	@Suppress("UNUSED_PARAMETER")
	private fun collect(annotation: Json.Property, roundEnvironment: RoundEnvironment, element: VariableElement) {
		val functionElement = element.enclosingElement as? ExecutableElement
			?: fail("cannot find enclosing function element")

		val typeElement = functionElement.enclosingElement
			?: fail("cannot find enclosing type element")

		val typeMeta = Meta.of(typeElement) as? MNamedType
			?: fail("must use annotation only on constructor parameters and properties")

		val jvmMethodSignature = functionElement.jvmMethodSignature

		val functionMeta = (typeMeta as? MFunctionContainer)?.functions?.firstOrNull { it.jvmSignature?.toString() == jvmMethodSignature }
		if (functionMeta != null) {
			if (functionMeta.source != MClassMemberSource.DECLARATION)
				return // ignore generated functions

			fail("must use annotation only on constructor parameters and properties")
		}

		val constructorMeta = (typeMeta as? MConstructable)?.constructors?.firstOrNull { it.jvmSignature?.toString() == jvmMethodSignature }
			?: fail("cannot find Kotlin metadata for constructor")

		val valueParameterMeta = constructorMeta.valueParameters.firstOrNull { element.simpleName.contentEquals(it.name.kotlin) }
			?: fail("cannot find Kotlin metadata for constructor value parameter")

		getOrCreateType(typeMeta.name).decodableProperties[MVariableName(element.simpleName.toString())] =
			CollectionResult.DecodableProperty(
				annotation = annotation,
				element = element,
				meta = valueParameterMeta
			)
	}


	private fun fail(message: String): Nothing =
		throw Fail(message)


	fun finish() = CollectionResult(
		codecProviders = codecProviders,
		types = types.values.mapNotNull { type ->
			try {
				finishType(type)
			}
			catch (e: Fail) {
				errorLogger.logError(e.message)
				null
			}
		}
	)


	private fun finishType(type: CollectedType): CollectionResult.Type {
		val typeName = type.name
		val typeMeta = type.meta ?: fail("using @Json.* annotations on members of type $typeName isn't allowed unless the type itself is annotated with @Json")

		return CollectionResult.Type(
			actualVisibility = type.actualVisibility
				?: fail("using @Json.* annotations on members of type $typeName isn't allowed unless the type itself is annotated with @Json"),

			annotation = type.annotation
				?: fail("using @Json.* annotations on members of type $typeName isn't allowed unless the type itself is annotated with @Json"),

			constructor = type.constructors.ifEmpty { null }?.let { constructors ->
				constructors.singleOrNull().also { singleConstructor ->
					singleConstructor ?: fail(
						"annotating multiple constructors on type $typeName with @Json.Constructor isn't allowed:\n" +
							constructors.joinToString(separator = "\n") { it.meta.toString() }
					)
				}
			},

			constructorExclusions = type.constructorExclusions.also { exclusions ->
				if (exclusions.isNotEmpty() && type.constructors.isNotEmpty())
					fail("using @Json.Excluded on constructors of type $typeName isn't allowed if explicitly selecting a constructor with @Json.Constructor")
			},

			customProperties = type.customProperties
				.mapValues { (_, customProperties) ->
					if (customProperties.size != 1) {
						fail("multiple @Json.CustomProperties-annotated extension functions on type $typeName cannot have the same name:\n" +
							customProperties.joinToString(separator = "\n") { element ->
								when (val extensionPackageName = element.extensionPackageName) {
									null -> "// in $typeName\n${element.functionMeta}"
									else -> "// in package $extensionPackageName\n${element.functionMeta}"
								}
							}
						)
					}

					customProperties.first()
				}
				.values,

			decodableProperties = type.decodableProperties,

			element = type.element
				?: fail("using @Json.* annotations on members of type $typeName isn't allowed unless the type itself is annotated with @Json"),

			meta = typeMeta,

			preferredCodecPackageName = type.preferredCodecPackageName,

			properties = type.properties
				.mapValues { (_, properties) ->
					if (properties.size != 1) {
						fail("multiple @Json.Property-annotated properties of type $typeName cannot have the same name:\n" +
							properties.joinToString(separator = "\n") { element ->
								when (val extensionPackageName = element.extensionPackageName) {
									null -> "// in $typeName\n${element.meta}"
									else -> "// in package $extensionPackageName\n${element.meta}"
								}
							}
						)
					}

					properties.first().also { property ->
						if (property.extensionPackageName != null) {
							val shadowingProperty = typeMeta.properties.firstOrNull {
								it.name == property.meta.name &&
									it.receiverParameterType == null &&
									(it.visibility == MVisibility.PUBLIC || it.visibility == MVisibility.INTERNAL)
							}
							if (shadowingProperty != null)
								fail("@Json.Property-annotated extension property is shadowed by a direct class member:\n" +
									"// in package ${property.extensionPackageName}\n${property.meta}\n" +
									"// in $typeName\n$shadowingProperty")
						}
					}
				},

			propertyExclusions = type.propertyExclusions
		)
	}


	private fun getOrCreateType(name: MQualifiedTypeName) =
		types.getOrPut(name) { CollectedType(name = name) }


	private inline fun withFailureHandling(annotationClass: KClass<out Annotation>, element: Element, block: () -> Unit) {
		try {
			block()
		}
		catch (e: Fail) {
			errorLogger.logError("@${MTypeName.of(annotationClass)} on ${element.debugName}: ${e.message}")
		}
	}


	private class CollectedType(
		val name: MQualifiedTypeName
	) {

		var actualVisibility: MVisibility? = null
		var annotation: Json? = null
		val constructors: MutableCollection<CollectionResult.Constructor> = mutableListOf()
		val constructorExclusions: MutableMap<MLocalId.Constructor, CollectionResult.ConstructorExclusion> = mutableMapOf()
		val customProperties: MutableMap<Pair<MFunctionName, Boolean>, MutableCollection<CollectionResult.CustomProperties>> = mutableMapOf()
		val decodableProperties: MutableMap<MVariableName, CollectionResult.DecodableProperty> = mutableMapOf()
		var element: TypeElement? = null
		var meta: MNamedType? = null
		var preferredCodecPackageName: MPackageName? = null
		val properties: MutableMap<MVariableName, MutableCollection<CollectionResult.Property>> = mutableMapOf()
		val propertyExclusions: MutableMap<MVariableName, CollectionResult.PropertyExclusion> = mutableMapOf()
	}


	private class Fail(override val message: String) : RuntimeException(message)
}
