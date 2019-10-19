package codecProvider

import io.fluidsonic.json.*


@Json.CodecProvider(
	externalTypes = [
		Json.ExternalType(KT30280::class, Json(codecPackageName = "externalType"), targetName = "codecProvider.KT30280"),
		Json.ExternalType(KT30280Primitive::class, Json(codecPackageName = "externalType"), targetName = "codecProvider.KT30280Primitive"),
		Json.ExternalType(Pair::class, Json(
			codecName = "ExternalPairCodec",
			codecPackageName = "externalType",
			codecVisibility = Json.CodecVisibility.publicRequired
		))
	]
)
interface CustomContextCodecProvider : JsonCodecProvider<CustomCodingContext>


interface CustomCodingContext : JsonCodingContext

inline class KT30280(val value: String)
inline class KT30280Primitive(val value: Double)
