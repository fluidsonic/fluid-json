package examples

import com.github.fluidsonic.fluid.json.*
import java.time.Instant


object CodingExample {

	@JvmStatic
	fun main(args: Array<String>) {
		// Using a codec for decoding AND encoding specific classes simplifies both, JSON parsing AND serialization :)

		val input = listOf(
			Event(id = 1, date = Instant.ofEpochSecond(1000000000), title = "One"),
			Event(id = 2, date = Instant.ofEpochSecond(2000000000), title = "Two"),
			Event(id = 3, date = Instant.ofEpochSecond(3000000000), title = "Three"),
			Event(id = 4, date = Instant.ofEpochSecond(4000000000), title = "Four"),
			Event(id = 5, date = Instant.ofEpochSecond(5000000000), title = "Five"),
			Event(id = 6, title = "Six")
		)

		val serializer = JSONCodingSerializer.builder()
			.encodingWith(EventCodec)
			.build()

		val json = serializer.serializeValue(input)

		val parser = JSONCodingParser.builder()
			.decodingWith(EventCodec)
			.build()

		val output = parser.parseValueOfType<List<Event>>(json)

		check(input == output) // to JSON and back!

		println(output)
	}


	private data class Event(
		val id: Int,
		val date: Instant? = null,
		val title: String
	)


	private object EventCodec : AbstractJSONCodec<Event, JSONCodingContext>() {

		override fun decode(valueType: JSONCodingType<in Event>, decoder: JSONDecoder<JSONCodingContext>): Event {
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


		override fun encode(value: Event, encoder: JSONEncoder<JSONCodingContext>) {
			encoder.writeIntoMap {
				writeMapElement("id", int = value.id)
				writeMapElement("date", value = value.date, skipIfNull = true)
				writeMapElement("title", string = value.title)
			}
		}
	}
}
