package com.github.fluidsonic.fluid.json.annotationprocessor

import com.github.fluidsonic.fluid.json.*
import com.github.fluidsonic.fluid.meta.*
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import java.io.File


internal class CodecProviderWriter(
	private val outputDirectory: File
) {

	fun write(configuration: CodecProviderConfiguration, codecNames: List<MQualifiedTypeName>) {
		FileSpec.builder(configuration.name.packageName.kotlin, configuration.name.withoutPackage().kotlin)
			.indent("\t")
			.addType(TypeSpec.classBuilder(configuration.name.withoutPackage().kotlin) // FIXME objectBuilder
				.applyIf(!configuration.isPublic) { addModifiers(KModifier.INTERNAL) }
				.addSuperinterface(
					codecProviderType,
					CodeBlock.of("JSONCodecProvider(${codecNames.sortedBy { it.kotlinInternal }.joinToString()})")
				)
				.build()
			)
			.build()
			.writeTo(outputDirectory)
	}


	companion object {

		private val contextType = JSONCodingContext::class.asTypeName()
		private val codecProviderType = JSONCodecProvider::class.asTypeName().parameterizedBy(contextType)
	}
}
