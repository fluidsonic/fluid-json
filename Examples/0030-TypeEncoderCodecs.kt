package examples

import com.github.fluidsonic.fluid.json.*
import java.time.Instant


object EncodingExample {

	@JvmStatic
	fun main(args: Array<String>) {
		// Using a codec for encoding specific Kotlin types simplifies JSON serialization a lot

		val serializer = JSONCodingSerializer.builder()
			.encodingWith(EventCodec)
			.build()

		val json = serializer.serializeValue(listOf(
			Event(id = 1, date = Instant.ofEpochSecond(1000000000), title = "One"),
			Event(id = 2, date = Instant.ofEpochSecond(2000000000), title = "Two"),
			Event(id = 3, date = Instant.ofEpochSecond(3000000000), title = "Three"),
			Event(id = 4, date = Instant.ofEpochSecond(4000000000), title = "Four"),
			Event(id = 5, date = Instant.ofEpochSecond(5000000000), title = "Five"),
			Event(id = 6, title = "Six")
		))

		println(json)
	}


	private data class Event(
		val id: Int,
		val date: Instant? = null,
		val title: String
	)


	private object EventCodec : AbstractJSONEncoderCodec<Event, JSONCodingContext>() {

		override fun encode(value: Event, encoder: JSONEncoder<JSONCodingContext>) {
			encoder.writeIntoMap {
				writeMapElement("id", int = value.id)
				writeMapElement("date", value = value.date, skipIfNull = true)
				writeMapElement("title", string = value.title)
			}
		}
	}
}
