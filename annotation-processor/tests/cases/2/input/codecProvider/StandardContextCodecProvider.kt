package codecProvider

import com.github.fluidsonic.fluid.json.*


@JSON.CodecProvider
interface StandardContextCodecProvider : JSONCodecProvider<JSONCodingContext>
