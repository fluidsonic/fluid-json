package com.github.fluidsonic.fluid.json


interface JSONCodecProvider<in Context : JSONCoderContext> {

	val decoderCodecs: List<JSONDecoderCodec<*, Context>>
	val encoderCodecs: List<JSONEncoderCodec<*, Context>>


	companion object
}
