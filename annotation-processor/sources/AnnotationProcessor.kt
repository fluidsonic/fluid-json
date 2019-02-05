package com.github.fluidsonic.fluid.json.annotationprocessor

import com.github.fluidsonic.fluid.json.*
import com.github.fluidsonic.fluid.meta.*
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.asTypeName
import sun.reflect.generics.parser.SignatureParser
import sun.reflect.generics.tree.MethodTypeSignature
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.PackageElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.tools.Diagnostic


@AutoService(Processor::class)
class AnnotationProcessor : AbstractProcessor() {

	private var codecProviderConfiguration: CodecProviderConfiguration? = null

	private val extensionMethodsByType: MutableMap<MQualifiedTypeName, MutableList<Pair<MPackageName, MFunctionName>>> = hashMapOf()
	private val extensionPropertiesByType: MutableMap<MQualifiedTypeName, MutableList<Triple<MProperty, JSON.Property, MPackageName>>> = hashMapOf()
	private val codecConfigurations: MutableList<CodecConfiguration> = mutableListOf()


	override fun getSupportedAnnotationTypes() =
		listOf(
			JSON::class,
			JSON.CodecProvider::class
		).mapTo(mutableSetOf()) { it.java.canonicalName }


	override fun getSupportedSourceVersion() =
		SourceVersion.RELEASE_8


	private fun logError(message: String) {
		processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, message)
	}


	private val outputDirectory
		get() = File(processingEnv.options[Options.generatedKotlinFilePath])


	override fun process(annotations: Set<TypeElement>, roundEnvironment: RoundEnvironment): Boolean {
		processCodecProviderAnnotations(roundEnvironment = roundEnvironment)

		processPropertyAnnotations(roundEnvironment = roundEnvironment)
		processCustomPropertiesAnnotations(roundEnvironment = roundEnvironment)

		processCodecAnnotations(roundEnvironment = roundEnvironment)

		if (roundEnvironment.processingOver())
			processCodecProviderConfigurations()

		return true
	}


	private fun processCodecAnnotation(annotation: JSON, element: Element, preferredPackageName: String? = null): String? {
		val type = Meta.of(element)
			?: return "must be a Kotlin type"

		if (type !is MClass || element !is TypeElement)
			return "must be a Kotlin class, is ${type::class.simpleName}"

		when (val kind = type.kind) {
			MClass.Kind.CLASS,
			MClass.Kind.COMPANION_OBJECT,
			MClass.Kind.DATA_CLASS,
			MClass.Kind.OBJECT ->
				Unit

			else ->
				return "must be a Kotlin class, is $kind"
		}

		val name = type.name
			?: return "must have a name"

		if (type.modality == MModality.ABSTRACT || type.modality == MModality.SEALED)
			return "must not be abstract or sealed"

		if (type.isInner)
			return "must not be an inner class"

		if (type.typeParameters.isNotEmpty())
			return "must not be generic"

		when (val visibility = type.visibility) {
			MVisibility.INTERNAL ->
				if (annotation.codecVisibility == JSON.Visibility.PUBLIC)
					return "must be public if codec is supposed to be public"

			MVisibility.PUBLIC ->
				Unit

			else ->
				return "must be internal or public but is $visibility"
		}

		var enclosingElement = element.enclosingElement
		while (enclosingElement != null && enclosingElement !is PackageElement) {
			val enclosingType = (Meta.of(enclosingElement) ?: return "its enclosing element '$enclosingElement' isn't a Kotlin type")
				as? MClass ?: return "its enclosing element '$enclosingElement' isn't a Kotlin class"

			when (val visibility = enclosingType.visibility) {
				MVisibility.INTERNAL ->
					if (annotation.codecVisibility == JSON.Visibility.PUBLIC)
						return "must only be nested in public types if codec is supposed to be public but '$enclosingElement' is $visibility"

				MVisibility.PUBLIC ->
					Unit

				else ->
					return "must only be nested in internal or public types but '$enclosingElement' is $visibility"
			}

			enclosingElement = enclosingElement.enclosingElement
		}

		var isDecodable = annotation.decodable
		val isObject = annotation.structure == JSON.Structure.OBJECT || (annotation.structure == JSON.Structure.DEFAULT && !type.isInline)

		val codecPackageName = annotation.codecPackageName
			.takeIf { it != "<default>" }
			?: preferredPackageName
			?: name.packageName.kotlin

		val decodableProperties = isDecodable.thenTake {
			type.primaryConstructor
				?.takeIf {
					it.visibility == MVisibility.INTERNAL || it.visibility == MVisibility.PUBLIC
				}
				?.valueParameters
				?.map { parameter ->
					if (parameter.isVariadic) return "must not have a vararg constructor"

					val parameterName = parameter.name
					val parameterType = parameter.type

					val propertyAnnotationElement = type.properties
						.firstOrNull { it.name == parameterName }
						?.let { property ->
							property.jvmSyntheticMethodForAnnotationsSignature?.name?.let { propertyAnnotationsMethodName ->
								element.enclosedElements
									.filterIsInstance<ExecutableElement>()
									.firstOrNull { it.simpleName.contentEquals(propertyAnnotationsMethodName) }
							}
						}

					val propertyAnnotation = propertyAnnotationElement?.getAnnotation(JSON.Property::class.java)

					CodecConfiguration.DecodableProperty(
						isNullable = parameterType.isNullable,
						name = parameterName,
						serializedName = propertyAnnotation?.serializedName?.takeIf { it != "<default>" }
							?: parameterName.toString(),
						presenceRequired = !parameterType.isNullable,
						type = parameterType.forKotlinPoet()
					)
				}
				?.sortedBy { it.serializedName } // FIXME check collisions
		} ?: emptyList()

		// FIXME add annotated extension properties
		val encodableProperties = type.properties
			.filter { property ->
				(property.visibility == MVisibility.INTERNAL || property.visibility == MVisibility.PUBLIC)
					&& property.kind != MProperty.Kind.SYNTHESIZED
					&& property.receiverParameter == null
			}
			.mapNotNull { property ->
				val propertyAnnotationElement = type.properties
					.firstOrNull { it.name == property.name }
					?.let { property ->
						property.jvmSyntheticMethodForAnnotationsSignature?.name?.let { propertyAnnotationsMethodName ->
							element.enclosedElements
								.filterIsInstance<ExecutableElement>()
								.firstOrNull { it.simpleName.contentEquals(propertyAnnotationsMethodName) }
						}
					}

				// FIXME check if added to to class without @JSON or non-encodable property
				val exclusionAnnotation = propertyAnnotationElement?.getAnnotation(JSON.Excluded::class.java)
				if (exclusionAnnotation != null) {
					return@mapNotNull null
				}

				// FIXME check if added to to class without @JSON or non-encodable property or excluded
				Triple(property, propertyAnnotationElement?.getAnnotation(JSON.Property::class.java), null)
			}
			.let {
				it + (extensionPropertiesByType[name] ?: emptyList())
			}
			.mapNotNull { (property, propertyAnnotation, extensionPackageName) ->
				when (annotation.encodableProperties) {
					JSON.EncodableProperties.ALL -> Unit

					JSON.EncodableProperties.ANNOTATED_ONLY ->
						if (propertyAnnotation == null)
							return@mapNotNull null

					JSON.EncodableProperties.DEFAULT ->
						if (propertyAnnotation == null && property.getterIsNotDefault)
							return@mapNotNull null
				}

				CodecConfiguration.EncodableProperty(
					importPackageName = extensionPackageName?.takeIf { it != MPackageName.fromKotlin(codecPackageName) },
					isNullable = property.returnType.isNullable,
					name = property.name,
					serializedName = propertyAnnotation?.serializedName?.takeIf { it != "<default>" }
						?: property.name.toString(),
					type = property.returnType.forKotlinPoet()
				)
			}
			.sortedBy { it.serializedName } // FIXME check collisions

		if (decodableProperties.isEmpty())
			isDecodable = false

		val isEncodable = encodableProperties.isNotEmpty() || annotation.encodableProperties != JSON.EncodableProperties.DEFAULT

		if (!isDecodable && !isEncodable)
			return "has no primary constructor suitable for decoding and no properties suitable for encoding"

		if (!isObject) {
			if (decodableProperties.size > 1) return "inline classes must have at most one decodable property, found ${decodableProperties.size}"
			if (encodableProperties.size > 1) return "inline classes must have at most one encodable property, found ${encodableProperties.size}"

			val decodableProperty = decodableProperties.singleOrNull()
			val encodableProperty = encodableProperties.singleOrNull()
			if (decodableProperty != null && encodableProperty != null && decodableProperty.type != encodableProperty.type)
				return "inline classes must decode from and encode to the same type (decodes from $decodableProperty, encodes to ${encodableProperty.type})"
		}

		val configuration = CodecConfiguration(
			contextType = codecProviderConfiguration?.contextType ?: ElementNames.codingContext,
			customPropertyMethods = extensionMethodsByType[name] ?: emptyList(),
			decodableProperties = decodableProperties,
			encodableProperties = encodableProperties,
			isEncodable = isEncodable,
			isDecodable = isDecodable,
			isObject = isObject,
			isPublic = annotation.codecVisibility == JSON.Visibility.PUBLIC,
			name = MQualifiedTypeName.fromKotlin(
				packageName = codecPackageName,
				typeName = annotation.codecName
					.takeIf { it != "<default>" }
					?: name.withoutPackage().kotlin.replace('.', '_') + "JSONCodec"
			),
			valueTypeName = name
		)

		codecConfigurations += configuration

		CodecWriter(outputDirectory = outputDirectory)
			.write(configuration = configuration)

		return null
	}


	private fun processCodecAnnotations(roundEnvironment: RoundEnvironment) {
		for (element in roundEnvironment.getElementsAnnotatedWith(JSON::class.java)) {
			val annotation = element.getAnnotation(JSON::class.java)

			processCodecAnnotation(annotation, element = element)?.let { errorMessage ->
				logError("@JSON is not valid for '$element': $errorMessage")
			}
		}
	}


	private fun processCodecProviderAnnotation(annotation: JSON.CodecProvider, element: Element): String? {
		codecProviderConfiguration?.let { configuration ->
			return "only one codec provider can be registered but ${configuration.name} also uses @JSON.CodecProvider"
		}

		val type = Meta.of(element)
			?: return "must be a Kotlin type"

		if (type !is MClass)
			return "must be a Kotlin interface, is ${type::class.simpleName}"

		if (type.kind != MClass.Kind.INTERFACE)
			return "must be a Kotlin interface, is ${type.kind}"

		val name = type.name
			?: return "must have a name"

		if (type.visibility != MVisibility.INTERNAL && type.visibility != MVisibility.PUBLIC)
			return "must have internal or public visibility but is ${type.visibility}"

		var enclosingElement = element.enclosingElement
		while (enclosingElement != null && enclosingElement !is PackageElement) {
			val enclosingType = (Meta.of(enclosingElement) ?: return "its enclosing element '$enclosingElement' isn't a Kotlin type")
				as? MClass ?: return "its enclosing element '$enclosingElement' isn't a Kotlin class"

			if (enclosingType.visibility != MVisibility.INTERNAL && enclosingType.visibility != MVisibility.PUBLIC)
				return "must only be nested in internal or public types but '$enclosingElement' is ${enclosingType.visibility}"

			enclosingElement = enclosingElement.enclosingElement
		}

		if (type.typeParameters.isNotEmpty())
			return "must not be generic"

		val supertype = type.supertypes.singleOrNull()
			?.takeIf { it.name == ElementNames.codecProviderType }
			?: return "must only extend the interface JSONCodecProvider, extends " +
				type.supertypes
					.filterNot { it.name == ElementNames.anyType }
					.joinToString { it.name?.toString() ?: "?" }
					.ifEmpty { "none" }

		codecProviderConfiguration = CodecProviderConfiguration(
			contextType = (supertype.arguments.first() as MTypeArgument.Type).type.name!!,// FIXME
			interfaceType = processingEnv.typeUtils.directSupertypes(element.asType())[1].asTypeName(),
			isPublic = type.visibility == MVisibility.PUBLIC,
			name = name
		)

		val annotationType = processingEnv.elementUtils.getTypeElement(JSON.CodecProvider::class.qualifiedName)
			?.asType()
			as? DeclaredType
			?: error("Cannot find type for annotation ${JSON.External::class}")

		val annotationMirror = element.getAnnotationMirror(annotationType)
		val externalTypeMirrors = annotationMirror?.getValue("externalTypes") as List<AnnotationMirror>? ?: emptyList()
		for ((index, externalTypeMirror) in externalTypeMirrors.withIndex()) {
			val externalType = annotation.externalTypes[index]

			val target = externalTypeMirror.getValue("target") as? DeclaredType
				?: error("Cannot properly parse type mirror: $externalTypeMirror")
			val targetElement = processingEnv.elementUtils.getTypeElement(target.toString())

			processCodecAnnotation(
				externalType.configuration,
				element = targetElement,
				preferredPackageName = name.packageName.kotlin
			)?.let { errorMessage ->
				logError("@JSON is not valid for $targetElement (through $element): $errorMessage")
			}
		}

		return null
	}


	private fun processCodecProviderAnnotations(roundEnvironment: RoundEnvironment) {
		for (element in roundEnvironment.getElementsAnnotatedWith(JSON.CodecProvider::class.java)) {
			val annotation = element.getAnnotation(JSON.CodecProvider::class.java)
			processCodecProviderAnnotation(annotation, element = element)?.let { errorMessage ->
				logError("@JSON.CodecProvider is not valid for '$element': $errorMessage")
			}
		}
	}


	private fun processCodecProviderConfigurations() {
		val codecNames = codecConfigurations.map { it.name }

		val writer = CodecProviderWriter(outputDirectory = outputDirectory)
		codecProviderConfiguration?.let { writer.write(configuration = it, codecNames = codecNames) }
	}


	// FIXME warnings for all continues, avoid for inline values
	private fun processCustomPropertiesAnnotations(roundEnvironment: RoundEnvironment) {
		for (element in roundEnvironment.getElementsAnnotatedWith(JSON.CustomProperties::class.java)) {
			val providerConfiguration = codecProviderConfiguration
				?: logError("a codec provider must be registered with @JSON.CodecProvider when using @JSON.CustomProperties")

			val element = element as? ExecutableElement ?: continue
			val classElement = element.enclosingElement as? TypeElement ?: continue
			val fileMeta = Meta.of(classElement) as? MFileFacade ?: continue
			val elementJvmMethodSignature = element.jvmMethodSignature
			val functionMeta = fileMeta.functions.firstOrNull { it.jvmSignature.toString() == elementJvmMethodSignature } ?: continue
			val receiverTypeReference = functionMeta.receiverParameter ?: continue
			if (receiverTypeReference.name != ElementNames.encoder) continue
			val valueTypeName = functionMeta.valueParameters.singleOrNull()?.type?.name ?: continue
			//val codecConfiguration = codecConfigurations.firstOrNull { it.valueTypeName == valueTypeName } ?: continue
			val packageName = MPackageName.fromKotlin(element.`package`.qualifiedName.toString())

			extensionMethodsByType.getOrPut(valueTypeName) { mutableListOf() } += packageName to functionMeta.name
		}
	}


	// FIXME warnings for all continues
	private fun processPropertyAnnotations(roundEnvironment: RoundEnvironment) {
		for (element in roundEnvironment.getElementsAnnotatedWith(JSON.Property::class.java)) {
			val element = element as? ExecutableElement ?: continue
			val classElement = element.enclosingElement as? TypeElement ?: continue
			val fileMeta = Meta.of(classElement) as? MFileFacade ?: continue
			val elementJvmMethodSignature = element.jvmMethodSignature
			val propertyMeta = fileMeta.properties
				.firstOrNull { it.jvmSyntheticMethodForAnnotationsSignature?.toString() == elementJvmMethodSignature }
				?: continue
			val receiverName = propertyMeta.receiverParameter?.name ?: continue
			val annotation = element.getAnnotation(JSON.Property::class.java) ?: continue
			val packageName = MPackageName.fromKotlin(element.`package`.qualifiedName.toString())

			extensionPropertiesByType.getOrPut(receiverName) { mutableListOf() } += Triple(propertyMeta, annotation, packageName)
		}
	}


	private object Options {

		const val generatedKotlinFilePath = "kapt.kotlin.generated"
	}
}


// FIXME
fun main() {
	SignatureParser.make().parseMethodSig("(Lservice/UmbrellaCompanyMember;)V").apply {
		val x: MethodTypeSignature
		println(this.exceptionTypes.joinToString())
		println(this.formalTypeParameters.joinToString())
		println(this.parameterTypes.joinToString())
		println(this.returnType.toString())
	}
}
