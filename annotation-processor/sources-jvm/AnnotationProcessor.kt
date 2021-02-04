package io.fluidsonic.json.annotationprocessor

import com.google.auto.service.*
import java.io.*
import javax.annotation.processing.*
import javax.lang.model.*
import javax.lang.model.element.*
import javax.tools.*


@AutoService(Processor::class)
public class AnnotationProcessor : AbstractProcessor(), ErrorLogger, TypeResolver {

	private val collectionPhase = CollectionPhase(
		errorLogger = this,
		typeResolver = this
	)


	override fun getSupportedAnnotationTypes(): Set<String> =
		collectionPhase.annotationClasses.mapTo(mutableSetOf()) { it.java.canonicalName }


	override fun getSupportedSourceVersion(): SourceVersion =
		SourceVersion.latestSupported()


	override fun logError(message: String) {
		processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, message)
	}


	override fun process(annotations: Set<TypeElement>, roundEnvironment: RoundEnvironment): Boolean {
		collectionPhase.collect(roundEnvironment = roundEnvironment)

		if (roundEnvironment.processingOver()) {
			val processingResult = ProcessingPhase(
				collectionResult = collectionPhase.finish(),
				errorLogger = this
			).process()

			GenerationPhase(
				outputDirectory = File(processingEnv.options[Options.generatedKotlinFilePath]!!),
				processingResult = processingResult
			).generate()
		}

		return true
	}


	override fun resolveType(qualifiedName: String): TypeElement? =
		processingEnv.elementUtils.getTypeElement(qualifiedName)


	private object Options {

		const val generatedKotlinFilePath = "kapt.kotlin.generated"
	}
}
