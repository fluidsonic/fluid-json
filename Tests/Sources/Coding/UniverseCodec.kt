package tests

import com.github.fluidsonic.fluid.json.JSONCodec
import com.github.fluidsonic.fluid.json.JSONDecoder
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.JSONException
import com.github.fluidsonic.fluid.json.readFromMapByElementValue
import com.github.fluidsonic.fluid.json.readListOfDecodableElements
import com.github.fluidsonic.fluid.json.writeIntoMap
import com.github.fluidsonic.fluid.json.writeMapElement


internal object UniverseCodec : JSONCodec<Universe, TestCoderContext> {

	override fun decode(decoder: JSONDecoder<out TestCoderContext>): Universe {
		var jaegers: List<Jaeger>? = null
		var kaijus: List<Kaiju>? = null

		decoder.readFromMapByElementValue { key ->
			when (key) {
				Keys.jaegers -> jaegers = readListOfDecodableElements()
				Keys.kaijus -> kaijus = readListOfDecodableElements()
				else -> skipValue()
			}
		}

		return Universe(
			jaegers = jaegers ?: throw JSONException("jaegers missing"),
			kaijus = kaijus ?: throw JSONException("kaijus missing")
		)
	}


	override fun encode(value: Universe, encoder: JSONEncoder<out TestCoderContext>) {
		encoder.writeIntoMap {
			writeMapElement(Keys.jaegers, encodable = value.jaegers)
			writeMapElement(Keys.kaijus, encodable = value.kaijus)
		}
	}


	override val decodableClass = Universe::class.java


	private object Keys {

		const val jaegers = "jaegers"
		const val kaijus = "kaijus"
	}
}
