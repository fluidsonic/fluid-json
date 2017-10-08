package com.github.fluidsonic.fluid.json

import org.apiguardian.api.API


@API(status = API.Status.EXPERIMENTAL)
internal interface JSONDecoderCodec<out Value : Any, in Context : JSONCoderContext> {

	fun decode(decoder: JSONDecoder<out Context>): Value
}
