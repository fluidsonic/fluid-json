@file:Suppress("INVISIBLE_MEMBER")

package io.fluidsonic.json

import io.ktor.client.features.json.JsonSerializer
import io.ktor.content.TextContent
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.util.reflect.*
import io.ktor.utils.io.core.*


public class FluidJsonSerializer(
	private val parser: JsonCodingParser<*> = JsonCodingParser.nonRecursive,
	private val serializer: JsonCodingSerializer = JsonCodingSerializer.nonRecursive
) : JsonSerializer {

	override fun read(type: TypeInfo, body: Input): Any =
		parser.parseValueOfType(body.readText(), JsonCodingType.of(type.reifiedType))


	override fun write(data: Any, contentType: ContentType): OutgoingContent =
		TextContent(serializer.serializeValue(data), contentType)
}
