package tests.coding

import com.github.fluidsonic.fluid.json.*
import org.junit.jupiter.api.*


internal object CompanionTest {

	@Test
	fun testInitialization() {
		// just make sure all companions initialize properly and we get more code coverage :)
		listOf(
			JSONCodingType.Companion,
			JSONCodec.Companion,
			JSONCodecProvider.Companion,
			JSONCodingContext.Companion,
			JSONDecoder.Companion,
			JSONDecoderCodec.Companion,
			JSONEncoder.Companion,
			JSONEncoderCodec.Companion,
			JSONCodingParser.Companion,
			JSONCodingSerializer.Companion
		)
	}
}
