package tests.coding

import io.fluidsonic.json.*
import org.junit.jupiter.api.*


internal object CompanionTest {

	@Test
	fun testInitialization() {
		// just make sure all companions initialize properly and we get more code coverage :)
		listOf(
			JsonCodingType.Companion,
			JsonCodec.Companion,
			JsonCodecProvider.Companion,
			JsonCodingContext.Companion,
			JsonDecoder.Companion,
			JsonDecoderCodec.Companion,
			JsonEncoder.Companion,
			JsonEncoderCodec.Companion,
			JsonCodingParser.Companion,
			JsonCodingSerializer.Companion
		)
	}
}
