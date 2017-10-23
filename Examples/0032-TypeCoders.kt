package examples

import com.github.fluidsonic.fluid.json.JSONCodec
import com.github.fluidsonic.fluid.json.JSONCoderContext
import com.github.fluidsonic.fluid.json.JSONDecoder
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.JSONException
import com.github.fluidsonic.fluid.json.JSONParser
import com.github.fluidsonic.fluid.json.JSONSerializer
import com.github.fluidsonic.fluid.json.parseListOfType
import com.github.fluidsonic.fluid.json.readDecodableOrNull
import com.github.fluidsonic.fluid.json.readFromMapByElementValue
import com.github.fluidsonic.fluid.json.serializeValue
import com.github.fluidsonic.fluid.json.writeIntoMap
import com.github.fluidsonic.fluid.json.writeMapElement
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


object CodingExample {

	@JvmStatic
	fun main(args: Array<String>) {
		// using a codec for decoding AND encoding specific classes simplifies both, JSON parsing AND serialization :)

		val input = listOf(
			Event(id = 1, date = Instant.ofEpochSecond(1000000000), title = "One"),
			Event(id = 2, date = Instant.ofEpochSecond(2000000000), title = "Two"),
			Event(id = 3, date = Instant.ofEpochSecond(3000000000), title = "Three"),
			Event(id = 4, date = Instant.ofEpochSecond(4000000000), title = "Four"),
			Event(id = 5, date = Instant.ofEpochSecond(5000000000), title = "Five"),
			Event(id = 6, title = "Six")
		)

		val serializer = JSONSerializer.builder()
			.encodingWith(EventCodec, InstantCodec)
			.build()

		val json = serializer.serializeValue(input)

		val parser = JSONParser.builder()
			.decodingWith(EventCodec, InstantCodec)
			.build()

		val output = parser.parseListOfType<Event>(json)

		check(input == output) // to JSON and back!

		println(output)
	}


	private data class Event(
		val id: Int,
		val date: Instant? = null,
		val title: String
	)


	private object EventCodec : JSONCodec<Event, JSONCoderContext> {

		override fun decode(decoder: JSONDecoder<out JSONCoderContext>): Event {
			var id: Int? = null
			var date: Instant? = null
			var title: String? = null

			decoder.readFromMapByElementValue { key ->
				when (key) {
					"id" -> id = readInt()
					"date" -> date = readDecodableOrNull()
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


		override fun encode(value: Event, encoder: JSONEncoder<out JSONCoderContext>) {
			encoder.writeIntoMap {
				writeMapElement("id", int = value.id)
				writeMapElement("date", encodable = value.date, skipIfNull = true)
				writeMapElement("title", string = value.title)
			}
		}


		override val decodableClass = Event::class
	}


	private object InstantCodec : JSONCodec<Instant, JSONCoderContext> {

		override fun decode(decoder: JSONDecoder<out JSONCoderContext>): Instant =
			decoder.readString().let {
				try {
					Instant.parse(it)
				}
				catch (e: DateTimeParseException) {
					throw JSONException("Cannot parse Instant '$it'", e)
				}
			}


		override fun encode(value: Instant, encoder: JSONEncoder<out JSONCoderContext>) {
			encoder.writeString(DateTimeFormatter.ISO_INSTANT.format(value))
		}


		override val decodableClass = Instant::class
	}
}
