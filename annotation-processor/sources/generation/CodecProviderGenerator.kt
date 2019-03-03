package com.github.fluidsonic.fluid.json.annotationprocessor

import com.github.fluidsonic.fluid.json.*
import com.github.fluidsonic.fluid.meta.*
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import java.io.File
import kotlin.reflect.KClass


internal class CodecProviderGenerator(
	private val outputDirectory: File
) {

	fun generate(codecProvider: ProcessingResult.CodecProvider, codecNames: Collection<MQualifiedTypeName>) {
		val typeName = codecProvider.name.withoutPackage().kotlin.replace('.', '_')

		val qualifiedTypeName = codecProvider.name.forKotlinPoet()
		val generatedQualifiedTypeName = ClassName(
			packageName = qualifiedTypeName.packageName,
			simpleName = "Generated" + typeName.replace('.', '_')
		)

		FileSpec.builder(qualifiedTypeName.packageName, generatedQualifiedTypeName.simpleName)
			.indent("\t")
			.addType(TypeSpec.objectBuilder(generatedQualifiedTypeName)
				.addModifiers(KModifier.PRIVATE)
				.addSuperinterface(qualifiedTypeName)
				.addSuperinterface(
					codecProvider.interfaceType.forKotlinPoet(),
					CodeBlock.of("JSONCodecProvider(${codecNames.sortedBy { it.kotlinInternal }.joinToString()})")
				)
				.build()
			)
			.addFunction(FunSpec.builder("generated")
				.applyIf(!codecProvider.isPublic) { addModifiers(KModifier.INTERNAL) }
				.addAnnotation(AnnotationSpec.builder(Suppress::class)
					.addMember("%S", "UNUSED_PARAMETER")
					.build()
				)
				.receiver(JSONCodecProvider.Companion::class)
				.addParameter(name = "interfaceClass", type = KClass::class.asTypeName().parameterizedBy(qualifiedTypeName))
				.returns(qualifiedTypeName)
				.addCode("return %T\n", generatedQualifiedTypeName)
				.build()
			)
			.build()
			.writeTo(outputDirectory)
	}
}
