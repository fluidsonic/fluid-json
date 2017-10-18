package tests

import com.github.fluidsonic.fluid.json.JSONCodec
import com.github.fluidsonic.fluid.json.JSONDecoder
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.JSONException
import com.github.fluidsonic.fluid.json.readDecodables
import com.github.fluidsonic.fluid.json.readMapByEntry
import com.github.fluidsonic.fluid.json.writeMap
import com.github.fluidsonic.fluid.json.writeMapEntry


internal object UniverseCodec : JSONCodec<Universe, TestCoderContext> {

	override fun decode(decoder: JSONDecoder<TestCoderContext>): Universe {
		var jaegers: List<Jaeger>? = null
		var kaijus: List<Kaiju>? = null

		decoder.readMapByEntry { key ->
			when (key) {
				Keys.jaegers -> jaegers = readDecodables()
				Keys.kaijus -> kaijus = readDecodables()
				else -> skipValue()
			}
		}

		return Universe(
			jaegers = jaegers ?: throw JSONException("jaegers missing"),
			kaijus = kaijus ?: throw JSONException("kaijus missing")
		)
	}


	override fun encode(value: Universe, encoder: JSONEncoder<out TestCoderContext>) {
		encoder.writeMap {
			writeMapEntry(Keys.jaegers, encodable = value.jaegers)
			writeMapEntry(Keys.kaijus, encodable = value.kaijus)
		}
	}


	override val valueClass = Universe::class.java


	private object Keys {

		const val jaegers = "jaegers"
		const val kaijus = "kaijus"
	}
}
