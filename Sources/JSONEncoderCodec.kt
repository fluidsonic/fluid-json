package com.github.fluidsonic.fluid.json

import org.apiguardian.api.API


@API(status = API.Status.EXPERIMENTAL)
internal interface JSONEncoderCodec<in Value : Any, in Context : JSONCoderContext> {

	fun encode(value: Value, encoder: JSONEncoder<out Context>)
}
