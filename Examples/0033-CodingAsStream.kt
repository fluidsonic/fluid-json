package examples

import com.github.fluidsonic.fluid.json.*
import java.io.StringWriter
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


object CodingAsStreamExample {

	@JvmStatic
	fun main(args: Array<String>) {
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
			.codecs(EventCodec, InstantCodec)
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
			.codecs(EventCodec, InstantCodec)
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


	private object EventCodec : AbstractJSONCodec<Event, JSONCoderContext>() {

		override fun decode(valueType: JSONCodableType<in Event>, decoder: JSONDecoder<JSONCoderContext>): Event {
			var id: Int? = null
			var date: Instant? = null
			var title: String? = null

			decoder.readFromMapByElementValue { key ->
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


		override fun encode(value: Event, encoder: JSONEncoder<JSONCoderContext>) {
			encoder.writeIntoMap {
				writeMapElement("id", int = value.id)
				writeMapElement("date", value = value.date, skipIfNull = true)
				writeMapElement("title", string = value.title)
			}
		}
	}


	private object InstantCodec : AbstractJSONCodec<Instant, JSONCoderContext>() {

		override fun decode(valueType: JSONCodableType<in Instant>, decoder: JSONDecoder<JSONCoderContext>): Instant =
			decoder.readString().let {
				try {
					Instant.parse(it)
				}
				catch (e: DateTimeParseException) {
					throw JSONException("Cannot parse Instant '$it'", e)
				}
			}


		override fun encode(value: Instant, encoder: JSONEncoder<JSONCoderContext>) {
			encoder.writeString(DateTimeFormatter.ISO_INSTANT.format(value))
		}
	}
}
