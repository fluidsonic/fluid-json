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

	private fun methodNameForReadingValueOfType(type: TypeName, nullable: Boolean) =
		when (type) {
			BOOLEAN -> "readBoolean"
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
			.let { if (nullable) "${it}OrNull" else it }


	private fun methodNameForWritingValueOfType(type: TypeName, nullable: Boolean) =
		when (type) {
			BOOLEAN -> "writeBoolean"
			BYTE -> "writeByte"
			CHAR -> "writeChar"
			DOUBLE -> "writeDouble"
			FLOAT -> "writeFloat"
			INT -> "writeInt"
			LONG -> "writeLong"
			SHORT -> "writeShort"
			STRING -> "writeString"
			else -> "writeValue"
		}
			.let { if (nullable) "${it}OrNull" else it }


	fun write(configuration: CodecConfiguration) {
		if (configuration.isObject)
			writeForObject(configuration)
		else
			writeForInline(configuration)
	}


	private fun writeForInline(configuration: CodecConfiguration) {
		val typeName = configuration.name.withoutPackage().kotlin.replace('.', '_')
		val valueQualifiedTypeName = configuration.valueTypeName.forKotlinPoet()

		val decodableProperty = configuration.decodableProperties.singleOrNull()
		val encodableProperty = configuration.encodableProperties.singleOrNull()

		val decoderType = JSONDecoder::class.asTypeName().parameterizedBy(configuration.contextType.forKotlinPoet())
		val encoderType = JSONEncoder::class.asTypeName().parameterizedBy(configuration.contextType.forKotlinPoet())

		FileSpec.builder(configuration.name.packageName.kotlin, typeName)
			.indent("\t")
			.addImport("com.github.fluidsonic.fluid.json",
				"readBooleanOrNull",
				"readByteOrNull",
				"readCharOrNull",
				"readDoubleOrNull",
				"readFloatOrNull",
				"readIntOrNull",
				"readLongOrNull",
				"readShortOrNull",
				"readStringOrNull",
				"readValueOfType",
				"readValueOfTypeOrNull"
			)
			.apply {
				if (encodableProperty != null) {
					encodableProperty.importPackageName?.let { addImport(it.kotlin, encodableProperty.name.toString()) }
				}
			}
			.addType(TypeSpec.objectBuilder(typeName)
				.applyIf(!configuration.isPublic) { addModifiers(KModifier.INTERNAL) }
				.superclass(
					when {
						decodableProperty == null -> AbstractJSONEncoderCodec::class
						encodableProperty == null -> AbstractJSONDecoderCodec::class
						else -> AbstractJSONCodec::class
					}.asTypeName().parameterizedBy(valueQualifiedTypeName, configuration.contextType.forKotlinPoet())
				)
				.apply {
					if (decodableProperty != null)
						addFunction(FunSpec.builder("decode")
							.addModifiers(KModifier.OVERRIDE)
							.receiver(decoderType)
							.addParameter("valueType", codingType.parameterizedBy(WildcardTypeName.consumerOf(valueQualifiedTypeName)))
							.returns(valueQualifiedTypeName)
							.addCode(
								"return %1T(%2N = %3N())",
								valueQualifiedTypeName,
								decodableProperty.name.toString(),
								methodNameForReadingValueOfType(decodableProperty.type, nullable = decodableProperty.isNullable)
							)
							.build())
				}
				.apply {
					if (encodableProperty != null)
						addFunction(FunSpec.builder("encode")
							.addModifiers(KModifier.OVERRIDE)
							.receiver(encoderType)
							.addParameter("value", valueQualifiedTypeName)
							.addCode(
								"%1N(value.%2N)\n",
								methodNameForWritingValueOfType(encodableProperty.type, nullable = encodableProperty.isNullable),
								encodableProperty.name.toString()
							)
							.build())
				}
				.build()
			)
			.build()
			.writeTo(outputDirectory)
	}


	private fun writeForObject(configuration: CodecConfiguration) {
		val typeName = configuration.name.withoutPackage().kotlin.replace('.', '_')
		val valueQualifiedTypeName = configuration.valueTypeName.forKotlinPoet()

		FileSpec.builder(configuration.name.packageName.kotlin, typeName)
			.indent("\t")
			.applyIf(configuration.isDecodable) {
				addImport("com.github.fluidsonic.fluid.json",
					"missingPropertyError",
					"readBooleanOrNull",
					"readByteOrNull",
					"readCharOrNull",
					"readDoubleOrNull",
					"readFloatOrNull",
					"readFromMapByElementValue",
					"readIntOrNull",
					"readLongOrNull",
					"readShortOrNull",
					"readStringOrNull",
					"readValueOfType",
					"readValueOfTypeOrNull"
				)
			}
			.applyIf(configuration.isEncodable) {
				addImport("com.github.fluidsonic.fluid.json",
					"writeIntoMap",
					"writeBooleanOrNull",
					"writeByteOrNull",
					"writeCharOrNull",
					"writeDoubleOrNull",
					"writeFloatOrNull",
					"writeIntOrNull",
					"writeLongOrNull",
					"writeShortOrNull",
					"writeStringOrNull",
					"writeValueOrNull",
					"writeMapElement"
				)

				configuration.encodableProperties
					.mapNotNull { property ->
						property.importPackageName?.let { property.name to it }
					}
					.forEach { (name, packageName) ->
						addImport(packageName.kotlin, name.toString())
					}

				configuration.customPropertyMethods
					.forEach { (packageName, name) ->
						addImport(packageName.kotlin, name.toString())
					}
			}
			.addType(TypeSpec.objectBuilder(typeName)
				.applyIf(!configuration.isPublic) { addModifiers(KModifier.INTERNAL) }
				.superclass(
					when {
						configuration.decodableProperties.isEmpty() -> AbstractJSONEncoderCodec::class
						configuration.encodableProperties.isEmpty() -> AbstractJSONDecoderCodec::class
						else -> AbstractJSONCodec::class
					}.asTypeName().parameterizedBy(valueQualifiedTypeName, configuration.contextType.forKotlinPoet())
				)
				.applyIf(configuration.isDecodable) {
					writeDecode(configuration = configuration, valueQualifiedTypeName = valueQualifiedTypeName)
				}
				.applyIf(configuration.isEncodable) {
					writeEncode(configuration = configuration, valueQualifiedTypeName = valueQualifiedTypeName)
				}
				.build()
			)
			.build()
			.writeTo(outputDirectory)
	}


	private fun TypeSpec.Builder.writeDecode(configuration: CodecConfiguration, valueQualifiedTypeName: ClassName): TypeSpec.Builder {
		val decoderType = JSONDecoder::class.asTypeName().parameterizedBy(configuration.contextType.forKotlinPoet())

		return addFunction(FunSpec.builder("decode")
			.addModifiers(KModifier.OVERRIDE)
			.receiver(decoderType)
			.addParameter("valueType", codingType.parameterizedBy(WildcardTypeName.consumerOf(valueQualifiedTypeName)))
			.returns(valueQualifiedTypeName)
			.apply {
				for (property in configuration.decodableProperties) {
					val propertyType = property.type
					val defaultValue: Any? = when {
						propertyType.isNullable || !propertyType.isPrimitive -> null
						propertyType == BOOLEAN -> false
						propertyType == CHAR -> 0.toChar()
						propertyType == DOUBLE -> 0.0
						propertyType == FLOAT -> 0.0f
						else -> 0 // FIXME what about other types
					}

					when {
						defaultValue is Char -> addStatement("var %1N = %2L.toChar()", "_${property.name}", defaultValue.toInt())
						defaultValue != null -> addStatement("var %1N = %2L", "_${property.name}", defaultValue)
						propertyType.isNullable -> addStatement("var %1N: %2T = null", "_${property.name}", propertyType)
						else -> addStatement("var %1N: %2T? = null", "_${property.name}", propertyType)
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
					val functionName = methodNameForReadingValueOfType(property.type, nullable = property.isNullable)

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
			.addCode("return %T(\n⇥", valueQualifiedTypeName)
			.apply {
				val size = configuration.decodableProperties.size
				for ((index, property) in configuration.decodableProperties.withIndex()) {
					val propertyType = property.type
					if (propertyType.isNullable || propertyType.isPrimitive) {
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
			.build()
		)
	}


	private fun TypeSpec.Builder.writeEncode(configuration: CodecConfiguration, valueQualifiedTypeName: ClassName): TypeSpec.Builder {
		val encoderType = JSONEncoder::class.asTypeName().parameterizedBy(configuration.contextType.forKotlinPoet())

		return addFunction(FunSpec.builder("encode")
			.addModifiers(KModifier.OVERRIDE)
			.receiver(encoderType)
			.addParameter("value", valueQualifiedTypeName)
			.beginControlFlow("writeIntoMap")
			.apply {
				for (property in configuration.encodableProperties) {
					val overload = when (property.type) {
						BOOLEAN -> "boolean"
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

				for ((_, functionName) in configuration.customPropertyMethods) {
					addStatement("%N(value)", functionName.toString())
				}
			}
			.endControlFlow()
			.build()
		)
	}


	private val TypeName.isPrimitive
		get() = setOf(BOOLEAN, BYTE, CHAR, DOUBLE, FLOAT, INT, LONG, SHORT).contains(this)


	companion object {

		private val codingType = JSONCodingType::class.asTypeName()

		private val STRING = ClassName("kotlin", "String")
	}
}
