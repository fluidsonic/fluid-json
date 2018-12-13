package com.github.fluidsonic.fluid.json.annotationprocessor

import com.github.fluidsonic.fluid.json.*
import com.github.fluidsonic.fluid.meta.*
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asTypeName
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic


@AutoService(Processor::class)
class AnnotationProcessor : AbstractProcessor() {

	private val codecConfigurations: MutableList<CodecConfiguration> = mutableListOf()
	private val codecProviderConfigurations: MutableList<CodecProviderConfiguration> = mutableListOf()


	override fun getSupportedAnnotationTypes() =
		listOf(
			JSON.Codec::class,
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
		processCodecAnnotations(roundEnvironment = roundEnvironment)
		processCodecProviderAnnotations(roundEnvironment = roundEnvironment)

		if (roundEnvironment.processingOver())
			processCodecProviderConfigurations()

		return true
	}


	private fun processCodecAnnotation(annotation: JSON.Codec, element: Element): String? {
		if (element.kind != ElementKind.CLASS) return "must be a class"

		val meta = Meta.of(element) ?: return "must be a Kotlin type"
		if (meta !is MClass) return "must be a Kotlin class, found ${meta::class.simpleName}"
		if (meta.modality == MModality.ABSTRACT || meta.modality == MModality.SEALED) return "must not be abstract or sealed"
		if (meta.isInner) return "must not be an inner class"

		when (val kind = meta.kind) {
			MClass.Kind.CLASS,
			MClass.Kind.COMPANION_OBJECT,
			MClass.Kind.DATA_CLASS,
			MClass.Kind.OBJECT ->
				Unit

			else ->
				return "must not be a/an $kind"
		}

		when (val visibility = meta.visibility) {
			MVisibility.INTERNAL ->
				if (annotation.visibility == JSON.Visibility.PUBLIC)
					return "must not be internal if codec is supposed to be public"

			MVisibility.PUBLIC ->
				Unit

			else ->
				return "must not be $visibility"
		}

		val name = meta.name ?: return "must have a name"

		val decodableProperties = meta.primaryConstructor
			?.takeIf {
				it.visibility == MVisibility.INTERNAL || it.visibility == MVisibility.PUBLIC
			}
			?.valueParameters
			?.map { parameter ->
				if (parameter.isVariadic) return "must not have a vararg constructor"

				val parameterName = parameter.name
				val parameterType = parameter.type
				val parameterTypeName = parameterType.name ?: return "has an unexpected type for parameter '$parameterName': $parameterType"

				CodecConfiguration.DecodableProperty(
					name = parameterName,
					serializedName = parameterName.toString(),
					presenceRequired = !parameterType.isNullable,
					type = ClassName(parameterTypeName.packageName.kotlin, parameterTypeName.withoutPackage().kotlin)
						.copy(nullable = parameterType.isNullable)
				)
			}
			?.sortedBy { it.serializedName }
			?: emptyList()

		val encodableProperties = meta.properties
			.filter { property ->
				(property.visibility == MVisibility.INTERNAL || property.visibility == MVisibility.PUBLIC)
					&& property.kind != MProperty.Kind.SYNTHESIZED
					&& property.receiverParameter == null
			}
			.map { property ->
				val propertyName = property.name
				val propertyType = property.returnType
				val propertyTypeName = propertyType.name ?: return "has an unexpected type for property '$propertyName': $propertyType"

				CodecConfiguration.EncodableProperty(
					name = propertyName,
					serializedName = propertyName.toString(),
					type = ClassName(propertyTypeName.packageName.kotlin, propertyTypeName.withoutPackage().kotlin)
						.copy(nullable = propertyType.isNullable)
				)
			}
			.sortedBy { it.serializedName }

		if (encodableProperties.isEmpty() && decodableProperties.isEmpty()) return "has no suitable primary constructor for decoding and no properties for encoding"

		val configuration = CodecConfiguration(
			decodableProperties = decodableProperties,
			encodableProperties = encodableProperties,
			isPublic = annotation.visibility == JSON.Visibility.PUBLIC,
			name = MQualifiedTypeName.fromKotlin(
				packageName = annotation.packageName
					.takeIf { it != "<default>" }
					?: name.packageName.kotlin,
				typeName = annotation.name
					.takeIf { it != "<default>" }
					?: name.withoutPackage().kotlin.replace('.', '_') + "JSONCodec"
			),
			typeName = element.asType().asTypeName()
		)

		codecConfigurations += configuration

		CodecWriter(outputDirectory = outputDirectory)
			.write(configuration = configuration)

		return null
	}


	private fun processCodecAnnotations(roundEnvironment: RoundEnvironment) {
		for (element in roundEnvironment.getElementsAnnotatedWith(JSON.Codec::class.java)) {
			val annotation = element.getAnnotation(JSON.Codec::class.java)

			processCodecAnnotation(annotation, element = element)?.let { errorMessage ->
				logError("@JSON.Codec is not invalid for '$element': $errorMessage")
			}
		}
	}


	private fun processCodecProviderAnnotation(annotation: JSON.CodecProvider, element: Element) {
		codecProviderConfigurations += CodecProviderConfiguration(
			isPublic = annotation.visibility == JSON.Visibility.PUBLIC,
			name = MQualifiedTypeName.fromKotlin(
				packageName = annotation.packageName
					.takeIf { it != "<default>" }
					?: processingEnv.elementUtils.getPackageOf(element).qualifiedName.toString(),
				typeName = annotation.name
					.takeIf { it != "<default>" }
					?: "GeneratedJSONCodecProvider"
			)
		)
	}


	private fun processCodecProviderAnnotations(roundEnvironment: RoundEnvironment) {
		for (element in roundEnvironment.getElementsAnnotatedWith(JSON.CodecProvider::class.java)) {
			val annotation = element.getAnnotation(JSON.CodecProvider::class.java)
			processCodecProviderAnnotation(annotation, element = element)
		}
	}


	private fun processCodecProviderConfigurations() {
		codecProviderConfigurations
			.groupBy { it.name }
			.filter { it.value.size > 1 }
			.ifEmpty { null }
			?.map { it.key }
			?.apply {
				forEach { logError("multiple instances @JSON.CodecProvider output type '$it'") }
				return
			}

		val codecNames = codecConfigurations.map { it.name }

		val writer = CodecProviderWriter(outputDirectory = outputDirectory)
		for (configuration in codecProviderConfigurations)
			writer.write(configuration = configuration, codecNames = codecNames)
	}


	private object Options {

		const val generatedKotlinFilePath = "kapt.kotlin.generated"
	}
}
