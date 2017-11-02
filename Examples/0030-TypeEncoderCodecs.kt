package examples

import com.github.fluidsonic.fluid.json.*
import java.time.Instant
import java.time.format.DateTimeFormatter


object EncodingExample {

	@JvmStatic
	fun main(args: Array<String>) {
		// Using a codec for encoding specific Kotlin types simplifies JSON serialization a lot

		val serializer = JSONSerializer.builder()
			.encodingWith(EventCodec, InstantCodec)
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


	private object EventCodec : AbstractJSONEncoderCodec<Event, JSONCoderContext>() {

		override fun encode(value: Event, encoder: JSONEncoder<JSONCoderContext>) {
			encoder.writeIntoMap {
				writeMapElement("id", int = value.id)
				writeMapElement("date", value = value.date, skipIfNull = true)
				writeMapElement("title", string = value.title)
			}
		}
	}


	private object InstantCodec : AbstractJSONEncoderCodec<Instant, JSONCoderContext>() {

		override fun encode(value: Instant, encoder: JSONEncoder<JSONCoderContext>) {
			encoder.writeString(DateTimeFormatter.ISO_INSTANT.format(value))
		}
	}
}
