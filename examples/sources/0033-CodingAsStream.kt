package examples

import com.github.fluidsonic.fluid.json.AbstractJSONCodec
import com.github.fluidsonic.fluid.json.JSONCodingContext
import com.github.fluidsonic.fluid.json.JSONCodingType
import com.github.fluidsonic.fluid.json.JSONDecoder
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.JSONException
import com.github.fluidsonic.fluid.json.JSONToken
import com.github.fluidsonic.fluid.json.readFromMapByElementValue
import com.github.fluidsonic.fluid.json.readValueOfType
import com.github.fluidsonic.fluid.json.use
import com.github.fluidsonic.fluid.json.writeIntoMap
import com.github.fluidsonic.fluid.json.writeMapElement
import java.io.StringWriter
import java.time.Instant


object CodingAsStreamExample {

	@JvmStatic
	fun main() {
		// Parsing and serialization using codecs is also supported when streaming by using JSONDecoder instead of
		// JSONReader and JSONEncoder instead of JSONWriter

		val input = listOf(
			Event(id = 1, date = Instant.ofEpochSecond(1000000000), title = "One"),
			Event(id = 2, date = Instant.ofEpochSecond(2000000000), title = "Two"),
			Event(id = 3, date = Instant.ofEpochSecond(3000000000), title = "Three"),
			Event(id = 4, date = Instant.ofEpochSecond(4000000000), title = "Four"),
			Event(id = 5, date = Instant.ofEpochSecond(5000000000), title = "Five"),
			Event(id = 6, title = "Six")
		)

		val json = encode(input)
		val output = decode(json)

		check(input == output) // to JSON and back!

		println(output)
	}


	private fun decode(json: String) =
		JSONDecoder.builder()
			.codecs(EventCodec)
			.source(json)
			.build()
			.use { decoder ->
				val output = mutableListOf<Event>()

				decoder.readListStart()
				while (decoder.nextToken != JSONToken.listEnd) {
					output.add(decoder.readValueOfType())
				}
				decoder.readListEnd()

				return@use output
			}


	private fun encode(input: List<Event>): String {
		val writer = StringWriter()

		return JSONEncoder.builder()
			.codecs(EventCodec)
			.destination(writer)
			.build()
			.use { encoder ->
				encoder.writeListStart()
				for (event in input) {
					encoder.writeValue(event)
				}
				encoder.writeListEnd()

				return@use writer.toString()
			}
	}


	private data class Event(
		val id: Int,
		val date: Instant? = null,
		val title: String
	)


	private object EventCodec : AbstractJSONCodec<Event, JSONCodingContext>() {

		override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<in Event>): Event {
			var id: Int? = null
			var date: Instant? = null
			var title: String? = null

			readFromMapByElementValue { key ->
				when (key) {
					"id" -> id = readInt()
					"date" -> date = readValueOfType()
					"title" -> title = readString()
					else -> skipValue()
				}
			}

			return Event(
				id = id ?: throw JSONException("missing id"),
				date = date,
				title = title ?: throw JSONException("missing title")
			)
		}


		override fun JSONEncoder<JSONCodingContext>.encode(value: Event) {
			writeIntoMap {
				writeMapElement("id", int = value.id)
				writeMapElement("date", value = value.date, skipIfNull = true)
				writeMapElement("title", string = value.title)
			}
		}
	}
}
