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
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import java.io.File
import kotlin.reflect.KClass


internal class CodecProviderWriter(
	private val outputDirectory: File
) {

	fun write(configuration: CodecProviderConfiguration, codecNames: List<MQualifiedTypeName>) {
		val typeName = configuration.name.withoutPackage().kotlin.replace('.', '_')

		val qualifiedTypeName = configuration.name.forKotlinPoet()
		val generatedQualifiedTypeName = ClassName(
			packageName = qualifiedTypeName.packageName,
			simpleName = "Generated" + typeName.replace('.', '_')
		)

		FileSpec.builder(qualifiedTypeName.packageName, generatedQualifiedTypeName.simpleName)
			.indent("\t")
			.addType(TypeSpec.classBuilder(generatedQualifiedTypeName) // FIXME objectBuilder
				.addModifiers(KModifier.PRIVATE)
				.addSuperinterface(qualifiedTypeName)
				.addSuperinterface(
					configuration.interfaceType,
					CodeBlock.of("JSONCodecProvider(${codecNames.sortedBy { it.kotlinInternal }.joinToString()})")
				)
				.build()
			)
			.addProperty(PropertySpec.builder("instance", generatedQualifiedTypeName)
				.addModifiers(KModifier.PRIVATE)
				.initializer("%T()", generatedQualifiedTypeName)
				.build()
			)
			.addFunction(FunSpec.builder("generated")
				.applyIf(!configuration.isPublic) { addModifiers(KModifier.INTERNAL) }
				.addAnnotation(AnnotationSpec.builder(Suppress::class)
					.addMember("%S", "UNUSED_PARAMETER")
					.build()
				)
				.receiver(JSONCodecProvider.Companion::class)
				.addParameter(name = "interfaceClass", type = KClass::class.asTypeName().parameterizedBy(qualifiedTypeName))
				.returns(qualifiedTypeName)
				.addCode("return instance\n")
				.build()
			)
			.build()
			.writeTo(outputDirectory)
	}
}
