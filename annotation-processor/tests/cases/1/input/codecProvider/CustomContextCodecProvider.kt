package codecProvider

import com.github.fluidsonic.fluid.json.*


@JSON.CodecProvider(
	externalTypes = [
		JSON.ExternalType(Pair::class, JSON(
			codecName = "ExternalPairCodec",
			codecPackageName = "externalType",
			codecVisibility = JSON.Visibility.publicRequired
		))
	]
)
interface CustomContextCodecProvider : JSONCodecProvider<CustomCodingContext>


interface CustomCodingContext : JSONCodingContext
