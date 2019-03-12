package com.github.fluidsonic.fluid.json.annotationprocessor

import com.github.fluidsonic.fluid.json.*
import com.github.fluidsonic.fluid.json.annotationprocessor.ProcessingResult.Codec.*
import com.github.fluidsonic.fluid.meta.*
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import java.io.File
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter


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
					addImport("com.github.fluidsonic.fluid.json",
						"writeValueOrNull"
					)
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
					}.asTypeName().parameterizedBy(codec.valueType, codec.contextType.forKotlinPoet())
				)
				.apply {
					if (decodableProperty != null)
						addFunction(FunSpec.builder("decode")
							.addModifiers(KModifier.OVERRIDE)
							.receiver(decoderType)
							.addParameter("valueType", codingType.parameterizedBy(codec.valueType))
							.returns(codec.valueType)
							.apply {
								val rawValueType = (codec.valueType as? ParameterizedTypeName)?.rawType ?: codec.valueType

								when {
									decodableProperty.typeParameterIndex >= 0 ->
										if (decodableProperty.type == KotlinpoetTypeNames.any || decodableProperty.type == KotlinpoetTypeNames.nullableAny)
											addCode(
												"return %1T(%2N = %3N(valueType.arguments[%4L]))",
												rawValueType,
												decodableProperty.name.toString(),
												methodNameForReadingValueOfType(decodableProperty.type),
												decodableProperty.typeParameterIndex
											)
										else
											addCode(
												"return %1T(%2N = %3N(valueType.arguments[%4L]) as %5T)",
												rawValueType,
												decodableProperty.name.toString(),
												methodNameForReadingValueOfType(decodableProperty.type),
												decodableProperty.typeParameterIndex,
												decodableProperty.type
											)

									else ->
										addCode(
											"return %1T(%2N = %3N())",
											rawValueType,
											decodableProperty.name.toString(),
											methodNameForReadingValueOfType(decodableProperty.type)
										)
								}
							}
							.build())
				}
				.apply {
					if (encodableProperty != null)
						addFunction(FunSpec.builder("encode")
							.addModifiers(KModifier.OVERRIDE)
							.receiver(encoderType)
							.addParameter("value", codec.valueType)
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

					val usesReflection = codec.decodingStrategy.properties.any { it.defaultValue == DecodableProperty.DefaultValue.defaultArgument }
					if (usesReflection) {
						addImport("kotlin.reflect", "KClass")
						addImport("com.github.fluidsonic.fluid.json", "jvmErasure")
					}
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
					}.asTypeName().parameterizedBy(codec.valueType, codec.contextType.forKotlinPoet())
				)
				.apply {
					if (codec.decodingStrategy != null) {
						generateDecode(codec = codec, strategy = codec.decodingStrategy, valueType = codec.valueType)
					}
					if (codec.encodingStrategy != null) {
						generateEncode(codec = codec, strategy = codec.encodingStrategy, valueType = codec.valueType)
					}
				}
				.build()
			)
			.build()
			.writeTo(outputDirectory)
	}


	private fun TypeSpec.Builder.generateDecode(
		codec: ProcessingResult.Codec,
		strategy: DecodingStrategy,
		valueType: TypeName
	) =
		if (strategy.properties.any { it.defaultValue == DecodableProperty.DefaultValue.defaultArgument })
			generateDecodeWithReflection(codec = codec, strategy = strategy, valueType = valueType)
		else
			generateDecodeWithoutReflection(codec = codec, strategy = strategy, valueType = valueType)


	private fun TypeSpec.Builder.generateDecodeWithReflection(
		codec: ProcessingResult.Codec,
		strategy: DecodingStrategy,
		valueType: TypeName
	): TypeSpec.Builder {
		val decoderType = JSONDecoder::class.asTypeName().parameterizedBy(codec.contextType.forKotlinPoet())
		val properties = strategy.properties.sortedBy { it.serializedName }
		val rawValueType = (valueType as? ParameterizedTypeName)?.rawType ?: valueType

		addProperty(PropertySpec.builder("constructor", KFunction::class.asTypeName().parameterizedBy(valueType))
			.addModifiers(KModifier.PRIVATE)
			.initializer(CodeBlock.builder()
				.beginControlFlow("%T::class.constructors.single·{·constructor·->", rawValueType)
				.addStatement("if (constructor.parameters.size != %L) return@single false\n", properties.size)
				.run {
					beginControlFlow("constructor.parameters.forEach { parameter ->")
					addStatement("val erasure = parameter.type.jvmErasure\n")
					run {
						beginControlFlow("when (parameter.name) {")
						run {
							properties.forEachIndexed { index, property ->
								val rawType = (property.type as? ParameterizedTypeName)?.rawType ?: property.type

								addStatement(
									"%1S -> if (parameter.index != %2L || parameter.isVararg || erasure != %3T::class) return@single false",
									property.name,
									index,
									rawType.copy(nullable = false)
								)
							}
							addStatement("else -> return@single false")
						}
						endControlFlow()
					}
					endControlFlow()

					add("\n")
					addStatement("return@single true")
				}
				.endControlFlow()
				.build()
			)
			.build()
		)

		properties.forEach { property ->
			addProperty(PropertySpec.builder("parameter_${property.name}", KParameter::class)
				.addModifiers(KModifier.PRIVATE)
				.initializer("constructor.parameters.first { it.name == %S }", property.name)
				.build()
			)
		}

		return addFunction(FunSpec.builder("decode")
			.addModifiers(KModifier.OVERRIDE)
			.receiver(decoderType)
			.addParameter("valueType", codingType.parameterizedBy(valueType))
			.returns(valueType)
			.addCode("val arguments = hashMapOf<KParameter, Any?>()\n")
			.addCode("readFromMapByElementValue { %N ->\n⇥", "key")
			.beginControlFlow("when (%N)", "key")
			.apply {
				for (property in properties) {
					val functionName = methodNameForReadingValueOfType(property.type)

					when {
						property.typeParameterIndex >= 0 ->
							if (property.type == KotlinpoetTypeNames.any || property.type == KotlinpoetTypeNames.nullableAny)
								addStatement(
									"%1S -> arguments[%2N] = %3N(valueType.arguments[%4L])",
									property.serializedName,
									"parameter_${property.name}",
									functionName,
									property.typeParameterIndex
								)
							else
								addStatement(
									"%1S -> arguments[%2N] = %3N(valueType.arguments[%4L]) as %5T",
									property.serializedName,
									"parameter_${property.name}",
									functionName,
									property.typeParameterIndex,
									property.type
								)

						functionName.startsWith("readValueOfType") -> // TODO refactor
							addStatement(
								"%1S -> arguments[%2N] = %3N<%4T>()",
								property.serializedName,
								"parameter_${property.name}",
								functionName,
								property.type.copy(nullable = false)
							)

						else ->
							addStatement(
								"%1S -> arguments[%2N] = %3N()",
								property.serializedName,
								"parameter_${property.name}",
								functionName
							)
					}
				}
				addStatement("else -> skipValue()")
			}
			.endControlFlow()
			.addCode("⇤}\n")
			.addCode("\n")
			.apply {
				var hasChecks = false
				properties.forEach { property ->
					when (property.defaultValue) {
						DecodableProperty.DefaultValue.nullReference -> {
							addStatement("if (!arguments.containsKey(%1N)) arguments[%1N] = null", "parameter_${property.name}")
							hasChecks = true
						}

						DecodableProperty.DefaultValue.none -> {
							addStatement("if (!arguments.containsKey(%1N)) missingPropertyError(%2S)", "parameter_${property.name}", property.serializedName)
							hasChecks = true
						}

						DecodableProperty.DefaultValue.defaultArgument ->
							Unit
					}
				}

				if (hasChecks)
					addCode("\n")

				addStatement("return constructor.callBy(arguments)")
			}
			.build()
		)
	}


	private fun TypeSpec.Builder.generateDecodeWithoutReflection(
		codec: ProcessingResult.Codec,
		strategy: DecodingStrategy,
		valueType: TypeName
	): TypeSpec.Builder {
		val decoderType = JSONDecoder::class.asTypeName().parameterizedBy(codec.contextType.forKotlinPoet())
		val properties = strategy.properties.sortedBy { it.serializedName }

		return addFunction(FunSpec.builder("decode")
			.addModifiers(KModifier.OVERRIDE)
			.receiver(decoderType)
			.addParameter("valueType", codingType.parameterizedBy(valueType))
			.returns(valueType)
			.apply {
				for (property in properties) {
					val localVariableName = "_${property.name}"

					val propertyType = property.type
					when {
						propertyType == KotlinpoetTypeNames.boolean -> addStatement("var %N = false", localVariableName)
						propertyType == KotlinpoetTypeNames.byte -> addStatement("var %N = 0.toByte()", localVariableName)
						propertyType == KotlinpoetTypeNames.char -> addStatement("var %N = 0.toChar()", localVariableName)
						propertyType == KotlinpoetTypeNames.double -> addStatement("var %N = 0.0", localVariableName)
						propertyType == KotlinpoetTypeNames.float -> addStatement("var %N = 0.0f", localVariableName)
						propertyType == KotlinpoetTypeNames.int -> addStatement("var %N = 0", localVariableName)
						propertyType == KotlinpoetTypeNames.long -> addStatement("var %N = 0L", localVariableName)
						propertyType == KotlinpoetTypeNames.short -> addStatement("var %N = 0.toShort()", localVariableName)
						propertyType.isNullable -> addStatement("var %1N: %2T = null", localVariableName, propertyType)
						else -> addStatement("var %1N: %2T? = null", localVariableName, propertyType)
					}

					if (property.type.isPrimitive)
						addStatement("var %1N = false", "${property.name}_isPresent")
				}
			}
			.addCode("\n")
			.addCode("readFromMapByElementValue { %N ->\n⇥", "key")
			.beginControlFlow("when (%N)", "key")
			.apply {
				for (property in properties) {
					val functionName = methodNameForReadingValueOfType(property.type)

					when {
						property.type.isPrimitive -> {
							beginControlFlow("%S -> ", property.serializedName)
							addStatement("%1N = %2N()", "_${property.name}", functionName)
							addStatement("%N = true", "${property.name}_isPresent")
							endControlFlow()
						}

						property.typeParameterIndex >= 0 ->
							if (property.type == KotlinpoetTypeNames.any || property.type == KotlinpoetTypeNames.nullableAny)
								addStatement(
									"%1S -> %2N = %3N(valueType.arguments[%4L])",
									property.serializedName,
									"_${property.name}",
									functionName,
									property.typeParameterIndex
								)
							else
								addStatement(
									"%1S -> %2N = %3N(valueType.arguments[%4L]) as %5T",
									property.serializedName,
									"_${property.name}",
									functionName,
									property.typeParameterIndex,
									property.type
								)

						else ->
							addStatement(
								"%1S -> %2N = %3N()",
								property.serializedName,
								"_${property.name}",
								functionName
							)
					}
				}
				addStatement("else -> skipValue()")
			}
			.endControlFlow()
			.addCode("⇤}\n")
			.addCode("\n")
			.apply {
				properties
					.filter { it.type.isPrimitive }
					.ifEmpty { null }
					?.apply {
						forEach { addStatement("%N || missingPropertyError(%S)", "${it.name}_isPresent", it.serializedName) }
						addCode("\n")
					}

				addCode("return %T(\n⇥", (valueType as? ParameterizedTypeName)?.rawType ?: valueType)
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
				addCode("⇤)\n")
			}
			.build()
		)
	}


	private fun TypeSpec.Builder.generateEncode(
		codec: ProcessingResult.Codec,
		strategy: EncodingStrategy,
		valueType: TypeName
	): TypeSpec.Builder {
		val encoderType = JSONEncoder::class.asTypeName().parameterizedBy(codec.contextType.forKotlinPoet())

		val customPropertyMethod = strategy.customPropertyMethods.sortedBy { it.second.kotlin }
		val properties = strategy.properties.sortedBy { it.serializedName }

		return addFunction(FunSpec.builder("encode")
			.addModifiers(KModifier.OVERRIDE)
			.receiver(encoderType)
			.addParameter("value", valueType)
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
			KotlinpoetTypeNames.boolean -> "readBoolean"
			KotlinpoetTypeNames.byte -> "readByte"
			KotlinpoetTypeNames.char -> "readChar"
			KotlinpoetTypeNames.double -> "readDouble"
			KotlinpoetTypeNames.float -> "readFloat"
			KotlinpoetTypeNames.int -> "readInt"
			KotlinpoetTypeNames.long -> "readLong"
			KotlinpoetTypeNames.short -> "readShort"
			KotlinpoetTypeNames.string -> "readString"
			else -> "readValueOfType"
		}
			.let { if (type.isNullable) "${it}OrNull" else it }


	private fun methodNameForWritingValueOfType(type: TypeName) =
		when (type) {
			KotlinpoetTypeNames.boolean -> "writeBoolean"
			KotlinpoetTypeNames.byte -> "writeByte"
			KotlinpoetTypeNames.char -> "writeChar"
			KotlinpoetTypeNames.double -> "writeDouble"
			KotlinpoetTypeNames.float -> "writeFloat"
			KotlinpoetTypeNames.int -> "writeInt"
			KotlinpoetTypeNames.long -> "writeLong"
			KotlinpoetTypeNames.short -> "writeShort"
			KotlinpoetTypeNames.string -> "writeString"
			else -> "writeValue"
		}
			.let { if (type.isNullable) "${it}OrNull" else it }


	private fun parameterNameForWritingValueOfType(type: TypeName) =
		when (type) {
			KotlinpoetTypeNames.boolean -> "boolean"
			KotlinpoetTypeNames.byte -> "byte"
			KotlinpoetTypeNames.char -> "char"
			KotlinpoetTypeNames.double -> "double"
			KotlinpoetTypeNames.float -> "float"
			KotlinpoetTypeNames.int -> "int"
			KotlinpoetTypeNames.long -> "long"
			KotlinpoetTypeNames.short -> "short"
			KotlinpoetTypeNames.string -> "string"
			else -> "value"
		}


	companion object {

		private val codingType = JSONCodingType::class.asTypeName()
	}
}
