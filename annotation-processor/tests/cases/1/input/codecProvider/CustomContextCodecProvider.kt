package codecProvider

import com.github.fluidsonic.fluid.json.*


@JSON.CodecProvider
interface CustomContextCodecProvider : JSONCodecProvider<CustomCodingContext>


interface CustomCodingContext : JSONCodingContext
