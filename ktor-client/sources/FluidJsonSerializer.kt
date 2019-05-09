@file:Suppress("INVISIBLE_MEMBER")

package com.github.fluidsonic.fluid.json

import io.ktor.client.call.*
import io.ktor.client.features.json.*
import io.ktor.client.response.*
import io.ktor.content.TextContent
import io.ktor.http.*
import io.ktor.http.content.*


class FluidJsonSerializer(
	private val parser: JSONCodingParser<*> = JSONCodingParser.nonRecursive,
	private val serializer: JSONCodingSerializer = JSONCodingSerializer.nonRecursive
) : JsonSerializer {

	override suspend fun read(type: TypeInfo, response: HttpResponse) =
		parser.parseValueOfType(response.readText(), JSONCodingType.of(type.reifiedType))


	override fun write(data: Any): OutgoingContent =
		TextContent(serializer.serializeValue(data), ContentType.Application.Json)
}
