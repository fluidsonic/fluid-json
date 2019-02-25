package com.github.fluidsonic.fluid.json.annotationprocessor

import com.github.fluidsonic.fluid.json.*
import com.github.fluidsonic.fluid.meta.*
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.WildcardTypeName
import com.squareup.kotlinpoet.asTypeName
import java.io.File


internal class CodecGenerator(
	private val outputDirectory: File
) {

	fun generate(codec: ProcessingResult.Codec) {
		if (codec.isSingleValue)
			generateForSingleValueRepresentation(codec)
		else
			generateForStructuredRepresentation(codec)
	}


	private fun generateForSingleValueRepresentation(codec: ProcessingResult.Codec) {
		val typeName = codec.name.withoutPackage().kotlin.replace('.', '_')
		val valueQualifiedTypeName = codec.valueTypeName.forKotlinPoet()

		val decodableProperty = codec.decodingStrategy?.properties?.singleOrNull()
		val encodableProperty = codec.encodingStrategy?.properties?.singleOrNull()

		val decoderType = JSONDecoder::class.asTypeName().parameterizedBy(codec.contextType.forKotlinPoet())
		val encoderType = JSONEncoder::class.asTypeName().parameterizedBy(codec.contextType.forKotlinPoet())

		FileSpec.builder(codec.name.packageName.kotlin, typeName)
			.indent("\t")
			.apply {
				if (decodableProperty != null) {
					addImport("com.github.fluidsonic.fluid.json",
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
				}
				if (encodableProperty != null) {
					encodableProperty.importPackageName?.let { addImport(it.kotlin, encodableProperty.name.kotlin) }
				}
			}
			.addType(TypeSpec.objectBuilder(typeName)
				.applyIf(!codec.isPublic) { addModifiers(KModifier.INTERNAL) }
				.superclass(
					when {
						decodableProperty == null -> AbstractJSONEncoderCodec::class
						encodableProperty == null -> AbstractJSONDecoderCodec::class
						else -> AbstractJSONCodec::class
					}.asTypeName().parameterizedBy(valueQualifiedTypeName, codec.contextType.forKotlinPoet())
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
								methodNameForReadingValueOfType(decodableProperty.type)
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
								methodNameForWritingValueOfType(encodableProperty.type),
								encodableProperty.name.toString()
							)
							.build())
				}
				.build()
			)
			.build()
			.writeTo(outputDirectory)
	}


	private fun generateForStructuredRepresentation(codec: ProcessingResult.Codec) {
		val typeName = codec.name.withoutPackage().kotlin.replace('.', '_')
		val valueQualifiedTypeName = codec.valueTypeName.forKotlinPoet()

		FileSpec.builder(codec.name.packageName.kotlin, typeName)
			.indent("\t")
			.apply {
				if (codec.decodingStrategy != null) {
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
				if (codec.encodingStrategy != null) {
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

					codec.encodingStrategy.properties
						.forEach { property ->
							if (property.importPackageName != null)
								addImport(property.importPackageName.kotlin, property.name.kotlin)
						}

					codec.encodingStrategy.customPropertyMethods
						.forEach { (packageName, name) ->
							if (packageName != null)
								addImport(packageName.kotlin, name.kotlin)
						}
				}
			}
			.addType(TypeSpec.objectBuilder(typeName)
				.applyIf(!codec.isPublic) { addModifiers(KModifier.INTERNAL) }
				.superclass(
					when {
						codec.decodingStrategy == null -> AbstractJSONEncoderCodec::class
						codec.encodingStrategy == null -> AbstractJSONDecoderCodec::class
						else -> AbstractJSONCodec::class
					}.asTypeName().parameterizedBy(valueQualifiedTypeName, codec.contextType.forKotlinPoet())
				)
				.apply {
					if (codec.decodingStrategy != null) {
						generateDecode(codec = codec, strategy = codec.decodingStrategy, valueQualifiedTypeName = valueQualifiedTypeName)
					}
					if (codec.encodingStrategy != null) {
						generateEncode(codec = codec, strategy = codec.encodingStrategy, valueQualifiedTypeName = valueQualifiedTypeName)
					}
				}
				.build()
			)
			.build()
			.writeTo(outputDirectory)
	}


	private fun TypeSpec.Builder.generateDecode(
		codec: ProcessingResult.Codec,
		strategy: ProcessingResult.Codec.DecodingStrategy,
		valueQualifiedTypeName: ClassName
	): TypeSpec.Builder {
		val decoderType = JSONDecoder::class.asTypeName().parameterizedBy(codec.contextType.forKotlinPoet())
		val properties = strategy.properties.sortedBy { it.serializedName }

		return addFunction(FunSpec.builder("decode")
			.addModifiers(KModifier.OVERRIDE)
			.receiver(decoderType)
			.addParameter("valueType", codingType.parameterizedBy(WildcardTypeName.consumerOf(valueQualifiedTypeName)))
			.returns(valueQualifiedTypeName)
			.apply {
				for (property in properties) {
					val localVariableName = "_${property.name}"

					val propertyType = property.type
					when {
						propertyType == TypeNames.boolean -> addStatement("var %N = false", localVariableName)
						propertyType == TypeNames.byte -> addStatement("var %N = 0.toByte()", localVariableName)
						propertyType == TypeNames.char -> addStatement("var %N = 0.toChar()", localVariableName)
						propertyType == TypeNames.double -> addStatement("var %N = 0.0", localVariableName)
						propertyType == TypeNames.float -> addStatement("var %N = 0.0f", localVariableName)
						propertyType == TypeNames.int -> addStatement("var %N = 0", localVariableName)
						propertyType == TypeNames.long -> addStatement("var %N = 0L", localVariableName)
						propertyType == TypeNames.short -> addStatement("var %N = 0.toShort()", localVariableName)
						propertyType.isNullable -> addStatement("var %1N: %2T = null", localVariableName, propertyType)
						else -> addStatement("var %1N: %2T? = null", localVariableName, propertyType)
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
				for (property in properties) {
					val functionName = methodNameForReadingValueOfType(property.type)

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
				properties
					.filter { it.presenceRequired }
					.ifEmpty { null }
					?.apply {
						forEach { addStatement("%N || missingPropertyError(%S)", "${it.name}_isPresent", it.serializedName) }
						addCode("\n")
					}
			}
			.addCode("return %T(\n⇥", valueQualifiedTypeName)
			.apply {
				val size = properties.size
				for ((index, property) in properties.withIndex()) {
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


	private fun TypeSpec.Builder.generateEncode(
		codec: ProcessingResult.Codec,
		strategy: ProcessingResult.Codec.EncodingStrategy,
		valueQualifiedTypeName: ClassName
	): TypeSpec.Builder {
		val encoderType = JSONEncoder::class.asTypeName().parameterizedBy(codec.contextType.forKotlinPoet())

		val customPropertyMethod = strategy.customPropertyMethods.sortedBy { it.second.kotlin }
		val properties = strategy.properties.sortedBy { it.serializedName }

		return addFunction(FunSpec.builder("encode")
			.addModifiers(KModifier.OVERRIDE)
			.receiver(encoderType)
			.addParameter("value", valueQualifiedTypeName)
			.beginControlFlow("writeIntoMap")
			.apply {
				for (property in properties)
					addStatement("writeMapElement(%1S, %2N = value.%3N)",
						property.serializedName,
						parameterNameForWritingValueOfType(property.type),
						property.name.toString()
					)

				for ((packageName, functionName) in customPropertyMethod)
					if (packageName != null)
						addStatement("%N(value)", functionName.toString())
					else
						addStatement("value.run { this@encode.%N() }", functionName.toString())
			}
			.endControlFlow()
			.build()
		)
	}


	private fun methodNameForReadingValueOfType(type: TypeName) =
		when (type) {
			TypeNames.boolean -> "readBoolean"
			TypeNames.byte -> "readByte"
			TypeNames.char -> "readChar"
			TypeNames.double -> "readDouble"
			TypeNames.float -> "readFloat"
			TypeNames.int -> "readInt"
			TypeNames.long -> "readLong"
			TypeNames.short -> "readShort"
			TypeNames.string -> "readString"
			else -> "readValueOfType"
		}
			.let { if (type.isNullable) "${it}OrNull" else it }


	private fun methodNameForWritingValueOfType(type: TypeName) =
		when (type) {
			TypeNames.boolean -> "writeBoolean"
			TypeNames.byte -> "writeByte"
			TypeNames.char -> "writeChar"
			TypeNames.double -> "writeDouble"
			TypeNames.float -> "writeFloat"
			TypeNames.int -> "writeInt"
			TypeNames.long -> "writeLong"
			TypeNames.short -> "writeShort"
			TypeNames.string -> "writeString"
			else -> "writeValue"
		}
			.let { if (type.isNullable) "${it}OrNull" else it }


	private fun parameterNameForWritingValueOfType(type: TypeName) =
		when (type) {
			TypeNames.boolean -> "boolean"
			TypeNames.byte -> "byte"
			TypeNames.char -> "char"
			TypeNames.double -> "double"
			TypeNames.float -> "float"
			TypeNames.int -> "int"
			TypeNames.long -> "long"
			TypeNames.short -> "short"
			TypeNames.string -> "string"
			else -> "value"
		}


	private val TypeName.isPrimitive
		get() = TypeNames.basic.contains(this)


	companion object {

		private val codingType = JSONCodingType::class.asTypeName()
	}


	private object TypeNames {

		val boolean = ClassName("kotlin", "Boolean")
		val byte = ClassName("kotlin", "Byte")
		val char = ClassName("kotlin", "Char")
		val double = ClassName("kotlin", "Double")
		val float = ClassName("kotlin", "Float")
		val int = ClassName("kotlin", "Int")
		val long = ClassName("kotlin", "Long")
		val short = ClassName("kotlin", "Short")
		val string = ClassName("kotlin", "String")

		val basic = setOf(
			boolean,
			byte,
			char,
			double,
			float,
			int,
			long,
			short
		)
	}
}
