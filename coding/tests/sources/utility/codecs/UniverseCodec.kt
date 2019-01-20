package tests.coding

import com.github.fluidsonic.fluid.json.AbstractJSONCodec
import com.github.fluidsonic.fluid.json.JSONCodingType
import com.github.fluidsonic.fluid.json.JSONDecoder
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.JSONException
import com.github.fluidsonic.fluid.json.readFromMapByElementValue
import com.github.fluidsonic.fluid.json.readValueOfType
import com.github.fluidsonic.fluid.json.writeIntoMap
import com.github.fluidsonic.fluid.json.writeMapElement


internal object UniverseCodec : AbstractJSONCodec<Universe, TestCoderContext>() {

	override fun JSONDecoder<TestCoderContext>.decode(valueType: JSONCodingType<in Universe>): Universe {
		var jaegers: List<Jaeger>? = null
		var kaijus: List<Kaiju>? = null

		readFromMapByElementValue { key ->
			when (key) {
				Keys.jaegers -> jaegers = readValueOfType()
				Keys.kaijus -> kaijus = readValueOfType()
				else -> skipValue()
			}
		}

		return Universe(
			jaegers = jaegers ?: throw JSONException("jaegers missing"),
			kaijus = kaijus ?: throw JSONException("kaijus missing")
		)
	}


	override fun JSONEncoder<TestCoderContext>.encode(value: Universe) {
		writeIntoMap {
			writeMapElement(Keys.jaegers, value = value.jaegers)
			writeMapElement(Keys.kaijus, value = value.kaijus)
		}
	}


	private object Keys {

		const val jaegers = "jaegers"
		const val kaijus = "kaijus"
	}
}
