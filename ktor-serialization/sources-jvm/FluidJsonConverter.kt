package io.fluidsonic.json

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.serialization.*
import io.ktor.util.reflect.*
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.*
import java.nio.charset.Charset


/**
 * A Ktor [ContentConverter] that uses fluid-json for serialization and deserialization.
 */
public class FluidJsonConverter(
	private val parser: JsonCodingParser<*> = JsonCodingParser.nonRecursive,
	private val serializer: JsonCodingSerializer = JsonCodingSerializer.nonRecursive,
) : ContentConverter {

	override suspend fun deserialize(charset: Charset, typeInfo: TypeInfo, content: ByteReadChannel): Any? =
		withContext(Dispatchers.IO) {
			parser.parseValueOfTypeOrNull(content.toInputStream().reader(charset), JsonCodingType.of(typeInfo.reifiedType))
		}


	override suspend fun serialize(contentType: ContentType, charset: Charset, typeInfo: TypeInfo, value: Any?): OutgoingContent =
		TextContent(serializer.serializeValue(value), contentType)
}


/**
 * Registers [FluidJsonConverter] as the content converter for the given [contentType].
 */
public fun Configuration.fluidJson(
	contentType: ContentType = ContentType.Application.Json,
	parser: JsonCodingParser<*> = JsonCodingParser.nonRecursive,
	serializer: JsonCodingSerializer = JsonCodingSerializer.nonRecursive,
) {
	register(
		contentType = contentType,
		converter = FluidJsonConverter(
			parser = parser,
			serializer = serializer,
		),
	)
}
