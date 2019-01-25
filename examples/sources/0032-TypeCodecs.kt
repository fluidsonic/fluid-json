package examples

import com.github.fluidsonic.fluid.json.*
import examples.CodingExample.Event
import examples.CodingExample.EventCodec
import java.time.Instant


fun main() {
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


private object CodingExample {


	data class Event(
		val id: Int,
		val date: Instant? = null,
		val title: String
	)


	object EventCodec : AbstractJSONCodec<Event, JSONCodingContext>() {

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
				id = id ?: missingPropertyError("id"),
				date = date,
				title = title ?: missingPropertyError("title")
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
