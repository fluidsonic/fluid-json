package tests

import com.github.fluidsonic.fluid.json.JSONCodec
import com.github.fluidsonic.fluid.json.JSONDecoder
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.JSONException
import com.github.fluidsonic.fluid.json.readElementsFromMap
import com.github.fluidsonic.fluid.json.readListOfDecodableElements
import com.github.fluidsonic.fluid.json.writeIntoMap
import com.github.fluidsonic.fluid.json.writeMapElement


internal object UniverseCodec : JSONCodec<Universe, TestCoderContext> {

	override fun decode(decoder: JSONDecoder<TestCoderContext>): Universe {
		var jaegers: List<Jaeger>? = null
		var kaijus: List<Kaiju>? = null

		decoder.readElementsFromMap { key ->
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


	override fun encode(value: Universe, encoder: JSONEncoder<TestCoderContext>) {
		encoder.writeIntoMap {
			writeMapElement(Keys.jaegers, encodable = value.jaegers)
			writeMapElement(Keys.kaijus, encodable = value.kaijus)
		}
	}


	override val valueClass = Universe::class.java


	private object Keys {

		const val jaegers = "jaegers"
		const val kaijus = "kaijus"
	}
}
