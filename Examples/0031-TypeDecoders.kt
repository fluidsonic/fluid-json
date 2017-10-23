package examples

import com.github.fluidsonic.fluid.json.JSONCoderContext
import com.github.fluidsonic.fluid.json.JSONDecoder
import com.github.fluidsonic.fluid.json.JSONDecoderCodec
import com.github.fluidsonic.fluid.json.JSONException
import com.github.fluidsonic.fluid.json.JSONParser
import com.github.fluidsonic.fluid.json.parseListOfType
import com.github.fluidsonic.fluid.json.readDecodableOrNull
import com.github.fluidsonic.fluid.json.readFromMapByElementValue
import java.time.Instant
import java.time.format.DateTimeParseException


object DecodingExample {

	@JvmStatic
	fun main(args: Array<String>) {
		// using a codec for decoding specific classes simplifies JSON parsing a lot

		val parser = JSONParser.builder()
			.decodingWith(EventCodec, InstantCodec)
			.build()

		val json = parser.parseListOfType<Event>("""
			[
			   {
			      "id":1,
			      "date":"2001-09-09T01:46:40Z",
			      "title":"One"
			   },
			   {
			      "id":2,
			      "date":"2033-05-18T03:33:20Z",
			      "title":"Two"
			   },
			   {
			      "id":3,
			      "date":"2065-01-24T05:20:00Z",
			      "title":"Three"
			   },
			   {
			      "id":4,
			      "date":"2096-10-02T07:06:40Z",
			      "title":"Four"
			   },
			   {
			      "id":5,
			      "date":"2128-06-11T08:53:20Z",
			      "title":"Five"
			   },
			   {
			      "id":6,
			      "title":"Six"
			   }
			]
		""")

		println(json)
	}


	private data class Event(
		val id: Int,
		val date: Instant? = null,
		val title: String
	)


	private object EventCodec : JSONDecoderCodec<Event, JSONCoderContext> {

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


		override val decodableClass = Event::class
	}


	private object InstantCodec : JSONDecoderCodec<Instant, JSONCoderContext> {

		override fun decode(decoder: JSONDecoder<out JSONCoderContext>): Instant =
			decoder.readString().let {
				try {
					Instant.parse(it)
				}
				catch (e: DateTimeParseException) {
					throw JSONException("Cannot parse Instant '$it'", e)
				}
			}


		override val decodableClass = Instant::class
	}
}
