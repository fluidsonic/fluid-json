package com.github.fluidsonic.fluid.json.annotationprocessor

import com.github.fluidsonic.fluid.json.*
import com.github.fluidsonic.fluid.meta.*
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.io.*
import kotlin.reflect.*


internal class CodecProviderGenerator(
	private val outputDirectory: File
) {

	fun generate(codecProvider: ProcessingResult.CodecProvider, codecNames: Collection<MQualifiedTypeName>) {
		val typeName = codecProvider.name.withoutPackage().kotlin.replace('.', '_')

		val qualifiedTypeName = codecProvider.name.forKotlinPoet()
		val generatedQualifiedTypeName = ClassName(
			packageName = qualifiedTypeName.packageName,
			simpleNames = listOf("Generated" + typeName.replace('.', '_'))
		)

		FileSpec.builder(qualifiedTypeName.packageName, generatedQualifiedTypeName.simpleName)
			.indent("\t")
			.apply {
				// TODO remove once fixed: https://github.com/square/kotlinpoet/pull/636
				// TODO was merged by now but is still broken somehow :O
				codecNames.forEach { codecName ->
					if (codecName.packageName.isRoot) addImport("", codecName.withoutPackage().kotlin)
				}
			}
			.addType(TypeSpec.objectBuilder(generatedQualifiedTypeName)
				.addModifiers(KModifier.PRIVATE)
				.addSuperinterface(qualifiedTypeName)
				.addSuperinterface(
					codecProvider.interfaceType.forKotlinPoet(typeParameters = emptyList()),
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
