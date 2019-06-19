@file:Suppress("INVISIBLE_MEMBER")

package com.github.fluidsonic.fluid.json

import io.ktor.client.call.*
import io.ktor.client.features.json.*
import io.ktor.content.TextContent
import io.ktor.http.*
import io.ktor.http.content.*
import kotlinx.io.core.*


class FluidJsonSerializer(
	private val parser: JSONCodingParser<*> = JSONCodingParser.nonRecursive,
	private val serializer: JSONCodingSerializer = JSONCodingSerializer.nonRecursive
) : JsonSerializer {

	override fun read(type: TypeInfo, body: Input) =
		parser.parseValueOfType(body.readText(), JSONCodingType.of(type.reifiedType))


	override fun write(data: Any, contentType: ContentType): OutgoingContent =
		TextContent(serializer.serializeValue(data), contentType)
}
