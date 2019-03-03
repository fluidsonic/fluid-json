package codecProvider

import com.github.fluidsonic.fluid.json.JSONCodecProvider
import com.github.fluidsonic.fluid.json.JSONCodingContext
import kotlin.Suppress
import kotlin.reflect.KClass

private object GeneratedStandardContextCodecProvider : StandardContextCodecProvider,
		JSONCodecProvider<JSONCodingContext> by JSONCodecProvider()

@Suppress("UNUSED_PARAMETER")
fun JSONCodecProvider.Companion.generated(interfaceClass: KClass<StandardContextCodecProvider>):
		StandardContextCodecProvider = GeneratedStandardContextCodecProvider
