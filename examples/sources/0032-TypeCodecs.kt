package examples

import com.github.fluidsonic.fluid.json.AbstractJSONCodec
import com.github.fluidsonic.fluid.json.JSONCodingContext
import com.github.fluidsonic.fluid.json.JSONCodingParser
import com.github.fluidsonic.fluid.json.JSONCodingSerializer
import com.github.fluidsonic.fluid.json.JSONCodingType
import com.github.fluidsonic.fluid.json.JSONDecoder
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.JSONException
import com.github.fluidsonic.fluid.json.parseValueOfType
import com.github.fluidsonic.fluid.json.readFromMapByElementValue
import com.github.fluidsonic.fluid.json.readValueOfType
import com.github.fluidsonic.fluid.json.serializeValue
import com.github.fluidsonic.fluid.json.writeIntoMap
import com.github.fluidsonic.fluid.json.writeMapElement
import java.time.Instant


object CodingExample {

	@JvmStatic
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
