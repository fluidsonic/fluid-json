package examples

import examples.CodingExample.Event
import examples.CodingExample.EventCodec
import io.fluidsonic.json.*
import java.time.*


fun main() {
	// Using a codec for decoding AND encoding specific classes simplifies both, JSON parsing AND serialization :)

	val original = listOf(
		Event(id = 1, date = Instant.ofEpochSecond(1000000000), title = "One", type = Event.Type.GOOD_NIGHT_OF_SLEEP),
		Event(id = 2, date = Instant.ofEpochSecond(2000000000), title = "Two", type = Event.Type.VERY_IMPORTANT_MEETING),
		Event(id = 3, date = Instant.ofEpochSecond(3000000000), title = "Three", type = Event.Type.GOOD_NIGHT_OF_SLEEP),
		Event(id = 4, date = Instant.ofEpochSecond(4000000000), title = "Four", type = Event.Type.GOOD_NIGHT_OF_SLEEP),
		Event(id = 5, date = Instant.ofEpochSecond(5000000000), title = "Five", type = Event.Type.MUM),
		Event(id = 6, title = "Six", type = Event.Type.GOOD_NIGHT_OF_SLEEP)
	)

	val serializer = JsonCodingSerializer.builder()
		.encodingWith(EventCodec)
		.build()

	val json = serializer.serializeValue(original)

	val parser = JsonCodingParser.builder()
		.decodingWith(EventCodec)
		.build()

	val parsed = parser.parseValueOfType<List<Event>>(json)

	check(original == parsed) // to JSON and back!

	println(parsed)
}


@Suppress("EnumEntryName")
private object CodingExample {


	data class Event(
		val id: Int,
		val date: Instant? = null,
		val title: String,
		val type: Type
	) {

		enum class Type {

			GOOD_NIGHT_OF_SLEEP,
			MUM,
			VERY_IMPORTANT_MEETING
		}
	}


	object EventCodec : AbstractJsonCodec<Event, JsonCodingContext>() {

		override fun JsonDecoder<JsonCodingContext>.decode(valueType: JsonCodingType<Event>): Event {
			var id: Int? = null
			var date: Instant? = null
			var title: String? = null
			var type: Event.Type? = null

			readFromMapByElementValue { key ->
				when (key) {
					"id" -> id = readInt()
					"date" -> date = readValueOfType()
					"title" -> title = readString()
					"type" -> type = readValueOfType()
					else -> skipValue()
				}
			}

			return Event(
				id = id ?: missingPropertyError("id"),
				date = date,
				title = title ?: missingPropertyError("title"),
				type = type ?: missingPropertyError("type")
			)
		}


		override fun JsonEncoder<JsonCodingContext>.encode(value: Event) {
			writeIntoMap {
				writeMapElement("id", int = value.id)
				writeMapElement("date", value = value.date, skipIfNull = true)
				writeMapElement("title", string = value.title)
				writeMapElement("type", value = value.type)
			}
		}
	}
}
