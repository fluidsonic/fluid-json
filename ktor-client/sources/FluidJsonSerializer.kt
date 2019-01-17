@file:Suppress("INVISIBLE_MEMBER")

package com.github.fluidsonic.fluid.json

import io.ktor.client.call.TypeInfo
import io.ktor.client.features.json.JsonSerializer
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.readText
import io.ktor.content.TextContent
import io.ktor.http.ContentType
import io.ktor.http.content.OutgoingContent


class FluidJsonSerializer(
	private val parser: JSONCodingParser = JSONCodingParser.nonRecursive,
	private val serializer: JSONCodingSerializer = JSONCodingSerializer.nonRecursive
) : JsonSerializer {

	override suspend fun read(type: TypeInfo, response: HttpResponse) =
		parser.parseValueOfType(response.readText(), JSONCodingType.of(type.reifiedType))


	override fun write(data: Any): OutgoingContent =
		TextContent(serializer.serializeValue(data), ContentType.Application.Json)
}
