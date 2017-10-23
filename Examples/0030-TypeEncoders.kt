package examples

import com.github.fluidsonic.fluid.json.JSONCoderContext
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.JSONEncoderCodec
import com.github.fluidsonic.fluid.json.JSONSerializer
import com.github.fluidsonic.fluid.json.serializeValue
import com.github.fluidsonic.fluid.json.writeIntoMap
import com.github.fluidsonic.fluid.json.writeMapElement
import java.time.Instant
import java.time.format.DateTimeFormatter


object EncodingExample {

	@JvmStatic
	fun main(args: Array<String>) {
		// using a codec for serializing specific classes simplifies JSON serialization a lot

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


	private object EventCodec : JSONEncoderCodec<Event, JSONCoderContext> {

		override fun encode(value: Event, encoder: JSONEncoder<out JSONCoderContext>) {
			encoder.writeIntoMap {
				writeMapElement("id", int = value.id)
				writeMapElement("date", encodable = value.date, skipIfNull = true)
				writeMapElement("title", string = value.title)
			}
		}


		override val encodableClasses = setOf(Event::class.java)
	}


	private object InstantCodec : JSONEncoderCodec<Instant, JSONCoderContext> {

		override fun encode(value: Instant, encoder: JSONEncoder<out JSONCoderContext>) {
			encoder.writeString(DateTimeFormatter.ISO_INSTANT.format(value))
		}


		override val encodableClasses = setOf(Instant::class.java)
	}
}
