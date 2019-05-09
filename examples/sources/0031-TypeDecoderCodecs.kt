package examples

import com.github.fluidsonic.fluid.json.*
import examples.DecodingExample.Event
import examples.DecodingExample.EventCodec
import java.time.*


fun main() {
	// Using a codec for decoding specific classes simplifies JSON parsing a lot

	val parser = JSONCodingParser.builder()
		.decodingWith(EventCodec)
		.build()

	val events = parser.parseValueOfType<List<Event>>("""
			[
			   {
			      "id":1,
			      "date":"2001-09-09T01:46:40Z",
			      "title":"One",
				  "type":"goodNightOfSleep"
			   },
			   {
			      "id":2,
			      "date":"2033-05-18T03:33:20Z",
			      "title":"Two",
				  "type":"veryImportantMeeting"
			   },
			   {
			      "id":3,
			      "date":"2065-01-24T05:20:00Z",
			      "title":"Three",
				  "type":"goodNightOfSleep"
			   },
			   {
			      "id":4,
			      "date":"2096-10-02T07:06:40Z",
			      "title":"Four",
				  "type":"goodNightOfSleep"
			   },
			   {
			      "id":5,
			      "date":"2128-06-11T08:53:20Z",
			      "title":"Five",
				  "type":"mum"
			   },
			   {
			      "id":6,
			      "title":"Six",
				  "type":"goodNightOfSleep"
			   }
			]
		""")

	println(events)
}


@Suppress("EnumEntryName")
private object DecodingExample {

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


	object EventCodec : AbstractJSONDecoderCodec<Event, JSONCodingContext>() {

		override fun JSONDecoder<JSONCodingContext>.decode(valueType: JSONCodingType<Event>): Event {
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
	}
}
