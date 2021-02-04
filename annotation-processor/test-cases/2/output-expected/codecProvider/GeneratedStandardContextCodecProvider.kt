package codecProvider

import io.fluidsonic.json.JsonCodecProvider
import io.fluidsonic.json.JsonCodingContext
import kotlin.Suppress
import kotlin.reflect.KClass

private object GeneratedStandardContextCodecProvider : StandardContextCodecProvider,
		JsonCodecProvider<JsonCodingContext> by JsonCodecProvider()

@Suppress("UNUSED_PARAMETER")
public
		fun JsonCodecProvider.Companion.generated(interfaceClass: KClass<StandardContextCodecProvider>):
		StandardContextCodecProvider = GeneratedStandardContextCodecProvider
