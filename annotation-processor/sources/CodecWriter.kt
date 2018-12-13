package com.github.fluidsonic.fluid.json.annotationprocessor

import com.github.fluidsonic.fluid.json.*
import com.github.fluidsonic.fluid.meta.*
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.BYTE
import com.squareup.kotlinpoet.CHAR
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.FLOAT
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.SHORT
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.WildcardTypeName
import com.squareup.kotlinpoet.asTypeName
import java.io.File


internal class CodecWriter(
	private val outputDirectory: File
) {

	fun write(configuration: CodecConfiguration) {
		FileSpec.builder(configuration.name.packageName.kotlin, configuration.name.withoutPackage().kotlin)
			.indent("\t")
			.applyIf(configuration.decodableProperties.isNotEmpty()) {
				addImport("com.github.fluidsonic.fluid.json",
					"missingPropertyError",
					"readFromMapByElementValue",
					"readValueOfType"
				)
			}
			.applyIf(configuration.encodableProperties.isNotEmpty()) {
				addImport("com.github.fluidsonic.fluid.json",
					"writeIntoMap",
					"writeMapElement"
				)
			}
			.addType(TypeSpec.objectBuilder(configuration.name.withoutPackage().kotlin)
				.applyIf(!configuration.isPublic) { addModifiers(KModifier.INTERNAL) }
				.superclass(
					when {
						configuration.decodableProperties.isEmpty() -> AbstractJSONEncoderCodec::class
						configuration.encodableProperties.isEmpty() -> AbstractJSONDecoderCodec::class
						else -> AbstractJSONCodec::class
					}.asTypeName().parameterizedBy(configuration.typeName, contextType)
				)
				.applyIf(configuration.decodableProperties.isNotEmpty()) {
					writeDecode(configuration = configuration)
				}
				.applyIf(configuration.encodableProperties.isNotEmpty()) {
					writeEncode(configuration = configuration)
				}
				.build()
			)
			.build()
			.writeTo(outputDirectory)
	}


	private fun TypeSpec.Builder.writeDecode(configuration: CodecConfiguration) =
		addFunction(FunSpec.builder("decode")
			.addModifiers(KModifier.OVERRIDE)
			.receiver(decoderType)
			.addParameter("valueType", codingType.parameterizedBy(WildcardTypeName.consumerOf(configuration.typeName)))
			.apply {
				for (property in configuration.decodableProperties) {
					val defaultValue: Any? = when {
						property.type.isNullable || !property.type.isPrimitive -> null
						property.type == BOOLEAN -> false
						property.type == CHAR -> 0.toChar()
						property.type == DOUBLE -> 0.0
						property.type == FLOAT -> 0.0f
						else -> 0
					}

					when {
						defaultValue is Char -> addStatement("var %1N = %2L.toChar()", "_${property.name}", defaultValue.toInt())
						defaultValue != null -> addStatement("var %1N = %2L", "_${property.name}", defaultValue)
						property.type.isNullable -> addStatement("var %1N: %2T = null", "_${property.name}", property.type)
						else -> addStatement("var %1N: %2T? = null", "_${property.name}", property.type)
					}

					if (property.presenceRequired) {
						addStatement("var %1N = false", "${property.name}_isPresent")
					}
				}
			}
			.addCode("\n")
			.addCode("readFromMapByElementValue { %N ->\n⇥", "key")
			.beginControlFlow("when (%N)", "key")
			.apply {
				for (property in configuration.decodableProperties) {
					val functionName = when (property.type.copy(nullable = false)) {
						BOOLEAN -> "readBool"
						BYTE -> "readByte"
						CHAR -> "readChar"
						DOUBLE -> "readDouble"
						FLOAT -> "readFloat"
						INT -> "readInt"
						LONG -> "readLong"
						SHORT -> "readShort"
						STRING -> "readString"
						else -> "readValueOfType"
					}

					if (property.presenceRequired) {
						beginControlFlow("%S -> ", property.serializedName)
						addStatement("%1N = %2N()", "_${property.name}", functionName)
						addStatement("%N = true", "${property.name}_isPresent")
						endControlFlow()
					}
					else {
						addStatement("%1S -> %2N = %3N()", property.serializedName, "_${property.name}", functionName)
					}
				}
				addStatement("else -> skipValue()")
			}
			.endControlFlow()
			.addCode("⇤}\n")
			.addCode("\n")
			.apply {
				configuration.decodableProperties
					.filter { it.presenceRequired }
					.ifEmpty { null }
					?.apply {
						forEach { addStatement("%N || missingPropertyError(%S)", "${it.name}_isPresent", it.serializedName) }
						addCode("\n")
					}
			}
			.addCode("return %T(\n⇥", configuration.typeName)
			.apply {
				val size = configuration.decodableProperties.size
				for ((index, property) in configuration.decodableProperties.withIndex()) {
					if (property.type.isNullable || property.type.isPrimitive) {
						addCode("%1N = %2N", property.name.toString(), "_${property.name}")
					}
					else {
						addCode("%1N = %2N ?: missingPropertyError(%3S)", property.name.toString(), "_${property.name}", property.serializedName)
					}
					if (index < size - 1) {
						addCode(",")
					}
					addCode("\n")
				}
			}
			.addCode("⇤)\n")
			.returns(configuration.typeName)
			.build()
		)


	private fun TypeSpec.Builder.writeEncode(configuration: CodecConfiguration) =
		addFunction(FunSpec.builder("encode")
			.addModifiers(KModifier.OVERRIDE)
			.receiver(encoderType)
			.addParameter("value", configuration.typeName)
			.beginControlFlow("writeIntoMap")
			.apply {
				for (property in configuration.encodableProperties) {
					val overload = when (property.type) {
						BOOLEAN -> "bool"
						BYTE -> "byte"
						CHAR -> "char"
						DOUBLE -> "double"
						FLOAT -> "float"
						INT -> "int"
						LONG -> "long"
						SHORT -> "short"
						STRING -> "string"
						else -> "value"
					}

					addStatement("writeMapElement(%1S, %2N = value.%3N)", property.serializedName, overload, property.name.toString())
				}
			}
			.endControlFlow()
			.build()
		)


	private val TypeName.isPrimitive
		get() = setOf(BOOLEAN, BYTE, CHAR, DOUBLE, FLOAT, INT, LONG, SHORT).contains(this)


	companion object {

		private val codingType = JSONCodingType::class.asTypeName()
		private val contextType = JSONCodingContext::class.asTypeName()
		private val decoderType = JSONDecoder::class.asTypeName().parameterizedBy(contextType)
		private val encoderType = JSONEncoder::class.asTypeName().parameterizedBy(contextType)

		private val STRING = ClassName("kotlin", "String")
	}
}
