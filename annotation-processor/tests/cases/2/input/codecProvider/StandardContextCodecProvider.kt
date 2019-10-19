package codecProvider

import io.fluidsonic.json.*


@Json.CodecProvider
interface StandardContextCodecProvider : JsonCodecProvider<JsonCodingContext>
