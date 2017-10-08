package tests

import com.github.fluidsonic.fluid.json.JSONCodec
import com.github.fluidsonic.fluid.json.JSONDecoder
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.JSONException
import com.github.fluidsonic.fluid.json.readMapByEntry
import com.github.fluidsonic.fluid.json.writeEntry
import com.github.fluidsonic.fluid.json.writeMap
import tests.Jaeger.Status
import java.time.LocalDate


internal object JaegerCodec : JSONCodec<Jaeger, TestCoderContext> {

	override fun decode(decoder: JSONDecoder<out TestCoderContext>): Jaeger {
		var height: Double? = null
		var lauchDate: LocalDate? = null
		var mark: Int? = null
		var name: String? = null
		var origin: String? = null
		var status: Status? = null
		var weight: Double? = null

		decoder.readMapByEntry { key ->
			when (key) {
				Keys.height -> height = readDouble()
				Keys.lauchDate -> lauchDate = readDecodable()
				Keys.mark -> mark = readInt()
				Keys.name -> name = readString()
				Keys.origin -> origin = readString()
				Keys.status -> status = readDecodable()
				Keys.weight -> weight = readDouble()
				else -> skipValue()
			}
		}

		return Jaeger(
			height = height ?: throw JSONException("height missing"),
			lauchDate = lauchDate ?: throw JSONException("lauchDate missing"),
			mark = mark ?: throw JSONException("mark missing"),
			name = name ?: throw JSONException("name missing"),
			origin = origin ?: throw JSONException("origin missing"),
			status = status ?: throw JSONException("status missing"),
			weight = weight ?: throw JSONException("weight missing")
		)
	}


	override fun encode(value: Jaeger, encoder: JSONEncoder<out TestCoderContext>) {
		encoder.writeMap {
			writeEntry(Keys.height, double = value.height)
			writeEntry(Keys.lauchDate, encodable = value.lauchDate)
			writeEntry(Keys.mark, int = value.mark)
			writeEntry(Keys.name, string = value.name)
			writeEntry(Keys.origin, string = value.origin)
			writeEntry(Keys.status, encodable = value.status)
			writeEntry(Keys.weight, double = value.weight)
		}
	}


	private object Keys {

		const val height = "height"
		const val lauchDate = "lauchDate"
		const val mark = "mark"
		const val name = "name"
		const val origin = "origin"
		const val status = "status"
		const val weight = "weight"
	}


	object StatusCodec : JSONCodec<Status, TestCoderContext> {

		override fun decode(decoder: JSONDecoder<out TestCoderContext>): Status {
			val id = decoder.readString()
			return when (id) {
				"destroyed" -> Status.destroyed
				else -> throw JSONException("Unknown status: $id")
			}
		}


		override fun encode(value: Status, encoder: JSONEncoder<out TestCoderContext>) {
			encoder.writeString(when (value) {
				Status.destroyed -> "destroyed"
			})
		}
	}
}
