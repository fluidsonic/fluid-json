package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.toBe
import ch.tutteli.atrium.verbs.assert
import com.github.fluidsonic.fluid.json.*
import org.junit.jupiter.api.Test


@Suppress("UNCHECKED_CAST")
internal object EnumJSONCodecProviderTest {

	@Test
	fun test() {
		val provider = EnumJSONCodecProvider(transformation = EnumJSONTransformation.Ordinal)
		val serializer = JSONCodingSerializer.builder().encodingWith(provider).build()

		for (enumClass in listOf(Example1::class, Example2::class)) {
			assert(serializer.serializeValue(enumClass.java.enumConstants.first())).toBe("0")
		}
	}


	private enum class Example1 {
		hello1
	}


	private enum class Example2 {
		hello2
	}
}
